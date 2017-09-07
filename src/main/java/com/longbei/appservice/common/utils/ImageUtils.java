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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	public static void main1(String[] args) throws Exception {

		int i=1;
		int j=2;
		InputStream imagein = new FileInputStream(i + ".jpg");
		InputStream imagein2 = new FileInputStream(j + ".png");

		BufferedImage image = ImageIO.read(imagein);
		BufferedImage image2 = ImageIO.read(imagein2);
		Graphics g = image.getGraphics();
		g.drawImage(image2, image.getWidth() - image2.getWidth() - 15, image.getHeight() - image2.getHeight() - 10,
				image2.getWidth() + 10, image2.getHeight() + 5, null);
		OutputStream outImage = new FileOutputStream("custom" + j + "-" + i + ".jpg");
		JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(outImage);
		enc.encode(image);
		imagein.close();
		imagein2.close();
		outImage.close();

	}


}
