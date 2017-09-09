package com.longbei.appservice.common.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等
 *
 * @author Bear.Xiong
 */
@SuppressWarnings("restriction")
public class ImageUtils {
	private static Logger log = LoggerFactory.getLogger(ImageUtils.class);

	/**
	 * 几种常见的图片格式
	 */
	public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop

	/**
	 * 缩放图像（按比例缩放）
	 * @param srcImageFile 源图像文件地址
	 * @param result 缩放后的图像地址
	 * @param scale 缩放比例
	 * @param flag 缩放选择:true 放大; false 缩小;
	 */
	public final static void scale(String srcImageFile, String result, int scale, boolean flag) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
			int width = src.getWidth(); // 源图宽度
			int height = src.getHeight(); // 源图高度
			if (flag) {// 放大
				width = width * scale;
				height = height * scale;
			} else {// 缩小
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			File outFile = new File(result);
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			ImageIO.write(tag, result.substring(result.lastIndexOf(".") + 1), outFile);// 输出到文件流
		} catch (IOException e) {
			log.debug("文件scale缩放失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 缩放图像（按高度和宽度缩放）
	 * @param srcImageFile 源图像文件地址
	 * @param result 缩放后的图像地址
	 * @param height 缩放后的高度
	 * @param width 缩放后的宽度
	 * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
	 */
	public final static void scale2(String srcImageFile, String result, int width, int height, boolean bb) {
		try {
			double ratio = 0.0; // 缩放比例
			File f = new File(srcImageFile);
			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			if (bi.getHeight() > bi.getWidth()) {
				ratio = (new Integer(height)).doubleValue() / bi.getHeight();
			} else {
				ratio = (new Integer(width)).doubleValue() / bi.getWidth();
			}
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
			itemp = op.filter(bi, null);
			// 补白
			if (bb) {
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null)) {
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
				} else {
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
				}
				g.dispose();
				itemp = image;
			}
			File outFile = new File(result);
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			ImageIO.write((BufferedImage) itemp, result.substring(result.lastIndexOf(".") + 1), outFile);
		} catch (IOException e) {
			log.debug("文件scale2缩放失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 创建图片缩略图
	 *
	 * @param srcImageFile
	 * @param scaledWidth
	 * @param scaledHeight
	 * @param preserveAlpha
	 * @return
	 */
	public static void scale3(String srcImageFile, String destImageFile, String type, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
		try {
			BufferedImage originalImage = ImageIO.read(new File(srcImageFile));
			int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
			BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
			Graphics2D g = scaledBI.createGraphics();
			if (preserveAlpha) {
				g.setComposite(AlphaComposite.Src);
			}
			g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
			g.dispose();
			ImageIO.write(scaledBI, type, new File(destImageFile));
		} catch (Exception e) {
			log.debug("创建图片缩略图！");
			e.printStackTrace();
		}
	}

	/**
	 * 获得图片宽高
	 * @param f
	 * @return
	 */
	public final static int[] getFileWidthHeight(File f) {
		int[] wh = new int[2];
		try {
			BufferedImage bi = ImageIO.read(f);
			wh[0] = bi.getWidth();
			wh[1] = bi.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wh;
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * @param srcImageFile 源图像地址
	 * @param result 切片后的图像地址
	 * @param x 目标切片起点坐标X
	 * @param y 目标切片起点坐标Y
	 * @param width 目标切片宽度
	 * @param height 目标切片高度
	 */
	public final static void cutImage(String srcImageFile, String result, int x, int y, int width, int height) {
		try {
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
				Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
				g.dispose();
				File outFile = new File(result);
				if (!outFile.exists()) {
					outFile.mkdirs();
				}
				// 输出为文件
				ImageIO.write(tag, result.substring(result.lastIndexOf(".") + 1), outFile);
			}
		} catch (Exception e) {
			log.debug("文件cutImage截取失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 图像切割（指定切片的行数和列数）
	 * @param srcImageFile 源图像地址
	 * @param descDir 切片目标文件夹
	 * @param rows 目标切片行数。默认2，必须是范围 [1, 20] 之内
	 * @param cols 目标切片列数。默认2，必须是范围 [1, 20] 之内
	 */
	public final static void cutImage2(String srcImageFile, String descDir, int rows, int cols) {
		int mRows = rows;
		int mCols = cols;
		try {
			if (rows <= 0 || rows > 20) {
				mRows = 2; // 切片行数
			}
			if (cols <= 0 || cols > 20) {
				mCols = 2; // 切片列数
			}
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int destWidth = srcWidth; // 每张切片的宽度
				int destHeight = srcHeight; // 每张切片的高度
				// 计算切片的宽度和高度
				if (srcWidth % mCols == 0) {
					destWidth = srcWidth / mCols;
				} else {
					destWidth = (int) Math.floor(srcWidth / mCols) + 1;
				}
				if (srcHeight % mRows == 0) {
					destHeight = srcHeight / mRows;
				} else {
					destHeight = (int) Math.floor(srcWidth / mRows) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < mRows; i++) {
					for (int j = 0; j < mCols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
						img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						File outFile = new File(descDir);
						if (!outFile.exists()) {
							outFile.mkdirs();
						}
						// 输出为文件
						ImageIO.write(tag, descDir.substring(srcImageFile.lastIndexOf(".") + 1), new File(descDir + "_r" + i + "_c" + j + srcImageFile.substring(srcImageFile.lastIndexOf("."))));
					}
				}
			}
		} catch (Exception e) {
			log.debug("文件cutImage2切割失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 图像切割（指定切片的宽度和高度）
	 * @param srcImageFile 源图像地址
	 * @param descDir 切片目标文件夹
	 * @param destWidth 目标切片宽度。默认200
	 * @param destHeight 目标切片高度。默认150
	 */
	public final static void cutImage3(String srcImageFile, String descDir, int destWidth, int destHeight) {
		int mWidth = destWidth;
		int mHeight = destHeight;
		try {
			if (destWidth <= 0) {
				mWidth = 200; // 切片宽度
			}
			if (destHeight <= 0) {
				mHeight = 150; // 切片高度
			}
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > mWidth && srcHeight > mHeight) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int cols = 0; // 切片横向数量
				int rows = 0; // 切片纵向数量
				// 计算切片的横向和纵向数量
				if (srcWidth % mWidth == 0) {
					cols = srcWidth / mWidth;
				} else {
					cols = (int) Math.floor(srcWidth / mWidth) + 1;
				}
				if (srcHeight % mHeight == 0) {
					rows = srcHeight / mHeight;
				} else {
					rows = (int) Math.floor(srcHeight / mHeight) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * mWidth, i * mHeight, mWidth, mHeight);
						img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						File outFile = new File(descDir);
						if (!outFile.exists()) {
							outFile.mkdirs();
						}
						// 输出为文件
						ImageIO.write(tag, srcImageFile.substring(srcImageFile.lastIndexOf(".") + 1), new File(descDir + "_r" + i + "_c" + j + srcImageFile.substring(srcImageFile.lastIndexOf("."))));
					}
				}
			}
		} catch (Exception e) {
			log.debug("文件cutImage3切割失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
	 * @param srcImageFile 源图像地址
	 * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destImageFile 目标图像地址
	 */
	public final static void convert(String srcImageFile, String formatName, String destImageFile) {
		try {
			File f = new File(srcImageFile);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			File outFile = new File(destImageFile);
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			ImageIO.write(src, formatName, new File(destImageFile));
		} catch (Exception e) {
			log.debug("文件格式转换失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 彩色转为黑白
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 目标图像地址
	 */
	public final static void gray(String srcImageFile, String destImageFile) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			File outFile = new File(destImageFile);
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			ImageIO.write(src, destImageFile.substring(destImageFile.lastIndexOf(".") + 1), new File(destImageFile));
		} catch (IOException e) {
			log.debug("文件黑白转换失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 给图片添加文字水印
	 * @param pressText 水印文字
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 目标图像地址
	 * @param fontName 水印的字体名称
	 * @param fontStyle 水印的字体样式
	 * @param color 水印的字体颜色
	 * @param fontSize 水印的字体大小
	 * @param x 修正值
	 * @param y 修正值
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressText(String pressText, String srcImageFile, String destImageFile, String fontName, int fontStyle, Color color, int fontSize, int x, int y,
			float alpha) {
		try {
			File img = new File(srcImageFile);
			Image src = ImageIO.read(img);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			// 在指定坐标绘制水印文字
			g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
			g.dispose();
			File outFile = new File(destImageFile);
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			ImageIO.write(image, destImageFile.substring(destImageFile.lastIndexOf(".") + 1), new File(destImageFile));// 输出到文件流
		} catch (Exception e) {
			log.debug("给图片添加文字水印pressText失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 给图片添加文字水印
	 * @param pressText 水印文字
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 目标图像地址
	 * @param fontName 字体名称
	 * @param fontStyle 字体样式
	 * @param color 字体颜色
	 * @param fontSize 字体大小
	 * @param x 修正值
	 * @param y 修正值
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressText2(String pressText, String srcImageFile, String destImageFile, String fontName, int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
		try {
			File img = new File(srcImageFile);
			Image src = ImageIO.read(img);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			// 在指定坐标绘制水印文字
			g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
			g.dispose();
			File outFile = new File(destImageFile);
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			ImageIO.write(image, destImageFile.substring(destImageFile.lastIndexOf(".") + 1), new File(destImageFile));
		} catch (Exception e) {
			log.debug("给图片添加文字水印pressText2失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 给图片添加图片水印
	 * @param pressImg 水印图片
	 * @param srcImageFile 源图像地址
	 * @param destImageFile 目标图像地址
	 * @param x 修正值。 默认在中间
	 * @param y 修正值。 默认在中间
	 * @param alpha 透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressImage(String pressImg, String srcImageFile, String destImageFile, int x, int y, float alpha) {
		try {
			File img = new File(srcImageFile);
			Image src = ImageIO.read(img);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			Image src_biao = ImageIO.read(new File(pressImg));
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao, null);
			// 水印文件结束
			g.dispose();
			File outFile = new File(destImageFile);
			if (!outFile.exists()) {
				outFile.mkdirs();
			}
			ImageIO.write(image, destImageFile.substring(destImageFile.lastIndexOf(".") + 1), new File(destImageFile));
		} catch (Exception e) {
			log.debug("给图片添加图片水印失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 计算text的长度（一个中文算两个字符）
	 * @param text
	 * @return
	 */
	public final static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}

	/**
	 * 生成背景透明的圆形图片，前提是图片为正方形
	 *
	 * @param srcImageFile
	 * @return
	 */
	public static void makeRoundedCorner(String srcImageFile, String destImageFile, String imageType) {
		try {
			BufferedImage image = ImageIO.read(new File(srcImageFile));
			int w = image.getWidth();
			int h = image.getHeight();
			BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = output.createGraphics();
			// 背景透明开始
			output = g2.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			g2.dispose();
			g2 = output.createGraphics();
			// 背景透明结束
			// This is what we want, but it only does hard-clipping, i.e. aliasing
			// g2.setClip(new RoundRectangle2D ...)
			// so instead fake soft-clipping by first drawing the desired clip shape
			// in fully opaque white with antialiasing enabled...
			g2.setComposite(AlphaComposite.Src);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//g2.setColor(Color.WHITE);
			g2.fill(new RoundRectangle2D.Float(0, 0, w, h, w, h));
			// ... then compositing the image on top,
			// using the white shape from above as alpha source
			g2.setComposite(AlphaComposite.SrcAtop);
			g2.drawImage(image, 0, 0, null);
			g2.dispose();
			ImageIO.write(output, imageType, new File(destImageFile));
		} catch (IOException e) {
			log.debug("生成图片圆角失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 *
	 * @param imgStr
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static boolean GenerateImage(String imgStr, String filePath, String fileName) {
		if (imgStr == null) {
			// 图像数据为空
			return false;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {
					// 调整异常数据
					bytes[i] += 256;
				}
			}
			String imgFilePath = FileUtils.getFilePath(filePath, fileName);
			// 生成图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 *
	 * @param imgFilePath
	 * @return
	 */
	public static String GetImageString(String imgFilePath) {
		File file = new File(imgFilePath);
		if (file.exists()) {
			byte[] data = null;
			// 读取图片字节数组
			try {
				InputStream in = new FileInputStream(imgFilePath);
				data = new byte[in.available()];
				in.read(data);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}// 对字节数组Base64编码
			BASE64Encoder encoder = new BASE64Encoder();
			// 返回Base64编码过的字节数组字符串
			return encoder.encode(data);
		} else {
			return "0";
		}
	}
	public static void main1(String[] args){
		List<String> paths = new ArrayList<>();
		paths.add("http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/livegift/ec5f288a-0370-40c6-9d39-0efb6548ebcb");
//		paths.add("http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/livegift/ca6a6c3d-54ab-47f9-a829-93d6de4ff55b");
//		paths.add("http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/livegift/d3c0075d-c140-4bd7-ab6e-5746e229e14f");
//		paths.add("http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/livegift/57a88e78-4d5d-4dfe-93b0-3a252c0afa11");
//		paths.add("http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/livegift/ec5f288a-0370-40c6-9d39-0efb6548ebcb");
//		paths.add("http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/livegift/ca6a6c3d-54ab-47f9-a829-93d6de4ff55b");
//		paths.add("http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/livegift/d3c0075d-c140-4bd7-ab6e-5746e229e14f");
//		paths.add("http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/livegift/57a88e78-4d5d-4dfe-93b0-3a252c0afa11");
//		paths.add("http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/livegift/ec5f288a-0370-40c6-9d39-0efb6548ebcb");
//		String dir = "/Users/smkk/image";
//		String groupId = "images";
//		try {
//			getCombinationOfhead(paths);
//		}catch (Exception e){
//			e.printStackTrace();
//		}

		makeRoundedCorner1(paths.get(0),"/Users/smkk/image/2.png","png",1000);
	}

	public static InputStream getCombinationOfhead(List<String> paths)
			throws IOException {
		List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
		// 压缩图片所有的图片生成尺寸同意的 为 50x50
		int w = 40;//小图的宽
		int h = 40;//小图的高
		int width = 120; // 这是画板的宽高
		int height = 120; // 这是画板的高度
		if(paths.size() > 4 ){// 40*3+5*4 照片压缩尺寸为40，间距为5
			w = 40;
			h = 40;
			width = 140;
			height = 140;
			// 50*2+5*3 照片压缩尺寸为50，间距为5
		}else if(paths.size()==4||paths.size()==3||paths.size()==2){
			w = 50;
			h = 50;
			width = 115;
			height = 115;
			// 100*1+10*2 照片压缩尺寸为100，间距为10
		}else if (paths.size()==1){
			w = 100;
			h = 100;
			width = 120;
			height = 120;
		}
		for (int i = 0; i < paths.size(); i++) {
			BufferedImage resize2 = resize2(paths.get(i), w, h, true);
			if(null != resize2){
				bufferedImages.add(resize2);
			}
		}
		// BufferedImage.TYPE_INT_RGB可以自己定义可查看API
		BufferedImage outImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 生成画布
		Graphics g = outImage.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		// 设置背景色透明
		g2d.setBackground(new Color(214, 214,214));
//		outImage = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
//		g2d.dispose();
//		g2d = outImage.createGraphics();

		// 通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
		g2d.clearRect(0, 0, width, height);
		// 开始拼凑 根据图片的数量判断该生成那种样式的组合头像目前为4中
		int j = 1;
		int k = 1;
		for (int i = 1; i <= bufferedImages.size(); i++) {
			if (bufferedImages.size()==9) {
				if (i<=3) {
					g2d.drawImage(bufferedImages.get(i - 1), 40 * i + 5 * i
							- 40, 5, null);
				}else if(i<=6) {
					g2d.drawImage(bufferedImages.get(i - 1),40 * j + 5 * j
							- 40, 50, null);
					j++;
				}else{
					g2d.drawImage(bufferedImages.get(i - 1),40 * k + 5 * k
							- 40,  95, null);
					k++;
				}
			}
			else if (bufferedImages.size()== 8) {
				if (i<=3) {
					g2d.drawImage(bufferedImages.get(i - 1), 40 * i + 5 * i
							- 40, 5, null);
				}else if(i<=6) {
					g2d.drawImage(bufferedImages.get(i - 1),40 * j + 5 * j
							- 40, 50, null);
					j++;
				}else{
					g2d.drawImage(bufferedImages.get(i - 1),40 * k + 22+5*k
							- 40,  95, null);
					k++;
				}

			}
			else if (bufferedImages.size()== 7) {
				if (i<=3) {
					g2d.drawImage(bufferedImages.get(i - 1), 40 * i + 5 * i
							- 40, 5, null);
				}else if(i<=6) {
					g2d.drawImage(bufferedImages.get(i - 1),40 * j + 5 * j
							- 40, 50, null);
					j++;
				}else{
					g2d.drawImage(bufferedImages.get(i - 1), 5 , 95, null);
				}

			}


			else    if (bufferedImages.size()== 6) {
				if (i<=3) {
					g2d.drawImage(bufferedImages.get(i - 1), 40 * i + 5 * i
							- 40, 27, null);
				}else {
					g2d.drawImage(bufferedImages.get(i - 1), 40 * j + 5 * j
							- 40, 72, null);
					j++;
				}
			}
			else if (bufferedImages.size()== 5) {
				if (i<=3) {
					g2d.drawImage(bufferedImages.get(i - 1), 40 * i + 5 * i
							- 40, 27, null);
				}else {
					g2d.drawImage(bufferedImages.get(i - 1),40 * k + 22+5*k
							- 40,  72, null);
					k++;
				}

			}
			else if (bufferedImages.size()== 4) {
				if (i <= 2) {
					g2d.drawImage(bufferedImages.get(i - 1), 50 * i + 5 * i
							- 50, 5, null);
				} else {
					g2d.drawImage(bufferedImages.get(i - 1), 50 * j + 5 * j
							- 50, 60, null);
					j++;
				}
			} else if (bufferedImages.size() == 3) {
				if (i <= 2) {

					g2d.drawImage(bufferedImages.get(i - 1), 50 * i + 5 * i
							- 50, 5, null);

				} else {

					g2d.drawImage(bufferedImages.get(i - 1), 32
							, 60, null);
					j++;
				}

			} else if (bufferedImages.size() == 2) {
				if(i == 1){
					g2d.drawImage(bufferedImages.get(i-1),  5,
							32, null);
				}else {
					g2d.drawImage(bufferedImages.get(i-1),  60,
							32, null);
				}
			} else if (bufferedImages.size() == 1) {
				g2d.drawImage(bufferedImages.get(i - 1), 10, 10, null);
			}

			// 需要改变颜色的话在这里绘上颜色。可能会用到AlphaComposite类
		}
//		RoundRectangle2D rect=new RoundRectangle2D.Double(0,0,width,height,20,20);
//		path.append(rect,false);
//		g2.setClip(path);
//		g2.drawImage(image,0,0,null);

//		BufferedImage bufferedImage = makeRoundedCorner(outImage,100);

//        new File(dir + "groupPicture" + File.separatorChar
//              + groupId.substring(0, 4) + File.separatorChar + groupId+".jpg")
//        String outPath = dir+groupId+".jpg";
//		String dir = "/Users/smkk/image";
//		String groupId = "images";

//		File file = File.createTempFile(UUID.randomUUID().toString(),".png");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();//存储图片文件byte数组
		ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
		ImageIO.write(outImage, "JPG", bos); //图片写入到 ImageOutputStream

//		FileInputStream fis = new FileInputStream(file);
		InputStream is = new ByteArrayInputStream(bos.toByteArray());

//		InputStream input = new ByteArrayInputStream(bos.toByteArray());
//		InputStream inputTmp = new ByteArrayInputStream(bos.toByteArray());
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		ImageIO.write(outImage, "JPG", os);
//		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
//		return ImageIO.write(outImage, format, file);
	}

	/**
	 * 图片压缩
	 * @param filePath
	 * @param height
	 * @param width
	 * @param bb
	 * @return
	 */
	public static BufferedImage resize2(String filePath, int height, int width,
										boolean bb) {
		try {
			double ratio = 0; // 缩放比例
//            System.out.println("图片缩放"+filePath);
			BufferedImage bi =null;
			if(filePath.indexOf("http://")==0){
				try {
					bi = ImageIO.read(new URL(filePath));
				}catch (Exception e){
					e.printStackTrace();
					ClassPathResource resource = new ClassPathResource("static/image/avatar_default.png");
					File file = resource.getFile();
					bi = ImageIO.read(file);
//					return null;
				}
			}else{
				bi = ImageIO.read(new File(filePath));
			}
			Image itemp = bi.getScaledInstance(width, height,
					Image.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue()
							/ bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(
						AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			if (bb) {
				// copyimg(filePath, "D:\\img");
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				g.dispose();
				itemp = image;
			}
			return (BufferedImage) itemp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static StringBuffer httpsRequest(String requestUrl, String dir) throws NoSuchAlgorithmException, NoSuchProviderException,
			KeyManagementException, MalformedURLException, IOException,
			ProtocolException, UnsupportedEncodingException {

		URL url = new URL(requestUrl);
		InputStream connection1 = url.openStream();
		try {
			byte[] bb=readInputStream(connection1);
			writeImageToDisk(bb,dir+".jpg");
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	public static void writeImageToDisk(byte[] img, String fileName){
		try {
			File file = new File(fileName);
			FileOutputStream fops = new FileOutputStream(file);
			fops.write(img);
			fops.flush();
			fops.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
		try {
			int w = image.getWidth();
			int h = image.getHeight();
			BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = output.createGraphics();
			output = g2.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			g2.dispose();
			g2 = output.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.fillRoundRect(0, 0,w, h, cornerRadius, cornerRadius);
			g2.setComposite(AlphaComposite.SrcIn);
			g2.drawImage(image, 0, 0, w, h, null);
			g2.dispose();
			return output;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String makeRoundedCorner1(String srcImageFile, String result, String type, int cornerRadius) {
		try {
//			bi = ImageIO.read(new URL(filePath));
			BufferedImage image = ImageIO.read(new URL(srcImageFile));
			int w = image.getWidth();
			int h = image.getHeight();
			BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = output.createGraphics();
			output = g2.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			g2.dispose();
			g2 = output.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.fillRoundRect(0, 0,w, h, cornerRadius, cornerRadius);
			g2.setComposite(AlphaComposite.SrcIn);
			g2.drawImage(image, 0, 0, w, h, null);
			g2.dispose();
			ImageIO.write(output, type, new File(result));
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}
