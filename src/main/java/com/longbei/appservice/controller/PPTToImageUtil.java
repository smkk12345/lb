package com.longbei.appservice.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

/**
 * Created by wangyongzhi 17/8/16.
 */
public class PPTToImageUtil {

    /**
     * 设置PPTX字体
     * @param slide
     */
    private static void setFont(XSLFSlide slide) {
        for (XSLFShape shape : slide.getShapes()) {
            if (shape instanceof XSLFTextShape) {
                XSLFTextShape txtshape = (XSLFTextShape)shape ;
                for (XSLFTextParagraph paragraph : txtshape.getTextParagraphs()) {
                    List<XSLFTextRun> truns = paragraph.getTextRuns();
                    for (XSLFTextRun trun : truns) {
                        trun.setFontFamily("宋体");
                    }
                }
            }

        }
    }
    /**
     * PPT转图片 （jpeg）(2007)
     * @param file
     * @param path 存放路径
     * @param picName 图片前缀名称 如 a 生成后为a_1,a_2 ...
     * @param picType 转成图片的类型，无点 如 jpg bmp png ...
     * @return true/false
     */
    public static boolean doPPTtoImage2007(File file,String path,String picName,String picType) {
        try {
            boolean isppt = checkFile(file);
            if (!isppt) {
                return false;
            }
            FileInputStream is = new FileInputStream(file);
            XMLSlideShow xmlSlideShow = new XMLSlideShow(is);
            XSLFSlide[] xslfSlides = xmlSlideShow.getSlides();
            Dimension pageSize = xmlSlideShow.getPageSize();

            is.close();
            for (int i = 0; i < xslfSlides.length; i++) {
                System.out.print("第" + i + "页。");
                setFont(xslfSlides[i]);
                BufferedImage img = new BufferedImage(pageSize.width,
                        pageSize.height, BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics = img.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);

                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pageSize.width,
                        pageSize.height));
                xslfSlides[i].draw(graphics);
                FileOutputStream out = new FileOutputStream(path+"/"+
                        + (i + 1) + "."+picType);
                javax.imageio.ImageIO.write(img, "jpeg", out);
                out.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//    public static boolean doPPTtoImag1(File file,String path,String picName,String picType) {
//        boolean isppt = checkFile(file);
//        ArrayList<String> imageList = new ArrayList<String>();
//        if (!isppt) {
//            return false;
//        }
//        File parentFile = new File(path);
//        if(!parentFile.exists()){
//            parentFile.mkdir();
//        }
//        try {
//            FileInputStream is = new FileInputStream(file);
//            SlideShow ppt = new SlideShow(is);
//            is.close();
//            // Dimension pgsize = ppt.getPageSize();
//            Slide[] slide = ppt.getSlides();
//            Dimension pgSize = ppt.getPageSize();
//            for (int i = 0; i < slide.length; i++) {
//                System.out.print("第" + i + "页。");
//                TextRun[] truns = slide[i].getTextRuns();
//                for (int k = 0; k < truns.length; k++) {
//                    RichTextRun[] rtruns = truns[k].getRichTextRuns();
//                    for (int l = 0; l < rtruns.length; l++) {
//                        int index = rtruns[l].getFontIndex();
//                        String name = rtruns[l].getFontName();
//                        rtruns[l].setFontIndex(1);
//                        rtruns[l].setFontName("宋体");
//                        System.out.println(rtruns[l].getText());
//                    }
//                }
//                BufferedImage img = new BufferedImage(pgSize.width,
//                        pgSize.height, BufferedImage.TYPE_INT_RGB);
//
//                Graphics2D graphics = img.createGraphics();
//                graphics.setPaint(Color.white);
//                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                        RenderingHints.VALUE_ANTIALIAS_ON);
//                graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
//                        RenderingHints.VALUE_RENDER_QUALITY);
//                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
//                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//                graphics.fill(new Rectangle2D.Float(0, 0, pgSize.width,
//                        pgSize.height));
//                slide[i].draw(graphics);
//
//                String tempPath =path+"/"+picName+"_" + (i + 1) + "."+picType;
//                FileOutputStream out = new FileOutputStream(tempPath);
//                javax.imageio.ImageIO.write(img, "jpeg", out);
//                imageList.add(tempPath);
//                out.close();
//            }
//            return false;
//        } catch (FileNotFoundException e) {
//            System.out.println(e);
//        } catch (IOException e) {
//        }
//        return false;
//    }



    /**
     * PPT转图片 （jpeg）(2003)
     * @param file
     * @param path 存放路径
     * @param picName 图片前缀名称 如 a 生成后为a_1,a_2 ...
     * @param picType 转成图片的类型，无点 如 jpg bmp png ...
     * @return true/false
     */
    public static List<String> doPPTtoImage(File file,String path,String picName,String picType) {
        boolean isppt = checkFile(file);
        ArrayList<String> imageList = new ArrayList<String>();
        if (!isppt) {
            return imageList;
        }
        File parentFile = new File(path);
        if(!parentFile.exists()){
            parentFile.mkdir();
        }
        try {
            FileInputStream is = new FileInputStream(file);
            SlideShow ppt = new SlideShow(is);
            is.close();
            // Dimension pgsize = ppt.getPageSize();
            Slide[] slide = ppt.getSlides();
            Dimension pgSize = ppt.getPageSize();
            for (int i = 0; i < slide.length; i++) {
                System.out.print("第" + i + "页。");

                TextRun[] truns = slide[i].getTextRuns();
                for (int k = 0; k < truns.length; k++) {
                    RichTextRun[] rtruns = truns[k].getRichTextRuns();
                    for (int l = 0; l < rtruns.length; l++) {
                        rtruns[l].setFontIndex(1);
                        rtruns[l].setFontName("宋体");
                        System.out.println(rtruns[l].getText());
                    }
                }

                BufferedImage img = new BufferedImage(pgSize.width,
                        pgSize.height, BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                graphics.fill(new Rectangle2D.Float(0, 0, pgSize.width,
                        pgSize.height));
                slide[i].draw(graphics);

                String tempPath =path+"/"+picName+"_" + (i + 1) + "."+picType;
                FileOutputStream out = new FileOutputStream(tempPath);
                javax.imageio.ImageIO.write(img, "jpeg", out);
                imageList.add(tempPath);
                out.close();
            }
            return imageList;
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
        }
        return imageList;
    }

    /**
     * PPT转图片 （jpeg）(2003)
     * @param file
     * @param path 存放路径
     * @param picName 图片前缀名称 如 a 生成后为a_1,a_2 ...
     * @param picType 转成图片的类型，无点 如 jpg bmp png ...
     * @return true/false
     */
    public static boolean doPPTtoImage(File file,String path,String picName,String picType, int w, int h) {
        boolean isppt = checkFile(file);
        if (!isppt) {
            return false;
        }
        try {
            FileInputStream is = new FileInputStream(file);
            SlideShow ppt = new SlideShow(is);
            is.close();
            // Dimension pgsize = ppt.getPageSize();
            Slide[] slide = ppt.getSlides();
            for (int i = 0; i < slide.length; i++) {
                System.out.print("第" + i + "页。");
                BufferedImage img = new BufferedImage(w,
                        h, BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, w,
                        h));
                slide[i].draw(graphics);

                FileOutputStream out = new FileOutputStream(path+"/"+picName+"_"
                        + (i + 1) + "."+picType);
                javax.imageio.ImageIO.write(img, "jpeg", out);
                out.close();
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
        }
        return false;
    }

    private static boolean checkFile(File file) {
        int pos = file.getName().lastIndexOf(".");
        String extName = "";
        if(pos >= 0) {
            extName = file.getName().substring(pos);
        }
        if(".ppt".equalsIgnoreCase(extName) || ".pptx".equalsIgnoreCase(extName)){
            return true;
        }
        return false;
    }
    /**
     * 根据ppt文件生成图片
     * @param sourceFile
     * @param pdfFile
     */
    @SuppressWarnings("deprecation")
    public static synchronized void ppt2007Img(String sourceFile,String pdfFile){
        try {
            FileInputStream is = new FileInputStream(sourceFile);
            XMLSlideShow ppt = new XMLSlideShow(is);
            is.close();
            Dimension pgsize = ppt.getPageSize();
            XSLFSlide[] slides = ppt.getSlides();
            for(int i=0;i<slides.length;i++){
                System.out.print("第" + i + "页。");
                //设置字体为宋体，解决中文乱码问题
                CTSlide xmlObject = slides[i].getXmlObject();
                CTGroupShape spTree = xmlObject.getCSld().getSpTree();
                CTShape[] spArray = spTree.getSpArray();
                for (CTShape shape : spArray) {
                    CTTextBody txBody = shape.getTxBody();
                    if(txBody == null){
                        continue;
                    }
                    CTTextParagraph[] pArray = txBody.getPArray();
                    CTTextFont font=CTTextFont.Factory.parse(
                            "<xml-fragment xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"+
                                    "<a:rPr lang=\"zh-CN\" altLang=\"en-US\" dirty=\"0\" smtClean=\"0\"> "+
                                    "<a:latin typeface=\"+mj-ea\"/> "+
                                    "</a:rPr>"+
                                    "</xml-fragment>");
                    for (CTTextParagraph textParagraph : pArray) {
                        CTRegularTextRun[] textRuns = textParagraph.getRArray();
                        for (CTRegularTextRun textRun : textRuns) {
                            CTTextCharacterProperties properties=textRun.getRPr();
                            properties.setLatin(font);
                        }
                    }
                }
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                slides[i].draw(graphics);
                FileOutputStream out = new FileOutputStream(new File(pdfFile+File.separator+i +".png"));
                ImageIO.write(img, "png", out);
                out.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }
    public static void renameFile(String path,String newName,String olName){
        if(!olName.equals(newName)){//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile=new File(path+"/"+olName);
            File newfile=new File(path+"/"+newName);
            if(!oldfile.exists()){
                return;//重命名文件不存在
            }
            if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newName+"已经存在！");
            else{
                oldfile.renameTo(newfile);
            }
        }else{
            System.out.println("新文件名和旧文件名相同...");
        }
    }
    public static String formatNum(String name){
        int index =0;
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            index = name.indexOf(matcher.group());
        }
        name = name.substring(index);
        name = name.substring(0,name.lastIndexOf("."));
        System.out.println(name);
        return name;
    }
    public static void ppt2003Img(String sourceFile, String tmpPicsDirName) {
        try {
            SlideShow ppt = new SlideShow(new HSLFSlideShow(sourceFile));
            Dimension pgsize = ppt.getPageSize();
            Slide[] slides = ppt.getSlides();
            for(int i=0;i<slides.length;i++){
                System.out.print("第" + i + "页。");
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                slides[i].draw(graphics);
                FileOutputStream out = new FileOutputStream(new File(tmpPicsDirName+File.separator+i +".png"));
                ImageIO.write(img, "png", out);
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
