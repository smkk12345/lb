package com.longbei.appservice.service.impl;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.service.OSSService;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.config.OssConfig;
import com.longbei.appservice.controller.PPTToImageUtil;
import com.longbei.appservice.dao.MediaResourceDetailMapper;
import com.longbei.appservice.dao.MediaResourceMapper;
import com.longbei.appservice.dao.MediaResourceTypeMapper;
import com.longbei.appservice.entity.MediaResource;
import com.longbei.appservice.entity.MediaResourceDetail;
import com.longbei.appservice.entity.MediaResourceType;
import com.longbei.appservice.service.MediaResourceService;
import com.longbei.appservice.service.api.staticresourceservice.PPTServiceApi;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by wangyongzhi 17/7/27.
 */
@Service("mediaResouceService")
public class MediaResourceServiceImpl implements MediaResourceService {
    private Logger logger = LoggerFactory.getLogger(MediaResourceServiceImpl.class);
    private static OfficeManager officeManager;
    public static final String FILETYPE_PNG = "png";
    public static final String SUFF_IMAGE = "." + FILETYPE_PNG;

    @Autowired
    private MediaResourceTypeMapper mediaResourceTypeMapper;
    @Autowired
    private MediaResourceMapper mediaResourceMapper;
    @Autowired
    private OSSService ossService;
    @Autowired
    private MediaResourceDetailMapper mediaResourceDetailMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private PPTServiceApi pptServiceApi;

    /**
     * 查询资源分类列表
     * @return
     */
    @Override
    public BaseResp<List<MediaResourceType>> findMediaResourceTypeList(Long userid) {
        logger.info("find MediaResouceType List ");
        BaseResp<List<MediaResourceType>> baseResp = new BaseResp<List<MediaResourceType>>();
        try{
            List<MediaResourceType> mediaResourceTypeList = this.mediaResourceTypeMapper.findMediaResourceTypeList(userid);
            if(mediaResourceTypeList == null){
                mediaResourceTypeList = new ArrayList<>();
            }
            baseResp.setData(mediaResourceTypeList);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.error("find MediaResouceType List error errorMsg:{} ",e);
        }

        return baseResp;
    }

    /**
     * 查询资源列表
     * @param mediaResource 查询的条件
     *        注意mediaResource中的source来源 必传,标识是web查询还是admin查询
     *        source 0.web 1.admin 是哪个平台查询的 必传 如果来源是web,则userid必传
     * @param pageno
     * @param pagesize
     * @return
     */
    @Override
    public BaseResp<Page<MediaResource>> findMediaResourceList(MediaResource mediaResource,String istranscoding, Integer pageno, Integer pagesize) {
        logger.info("find mediaResouce list mediaResource:{} pageno:{} pagesize:{}",mediaResource.toString(),pageno,pagesize);
        BaseResp<Page<MediaResource>> baseResp = new BaseResp<>();
        try{
            int count = 0;
            List<MediaResource> mediaResourceList= new ArrayList<>();
            count = this.mediaResourceMapper.findMediaResourceCount(mediaResource,istranscoding);
            if(count > 0){
                int startno = (pageno-1)*pagesize;
                mediaResourceList = this.mediaResourceMapper.findMediaResourceList(mediaResource,istranscoding,startno,pagesize);
            }

            Page<MediaResource> pageinfo = new Page<MediaResource>();
            pageinfo.setList(mediaResourceList);
            pageinfo.setTotalCount(count);
            pageinfo.setCurrentPage(pageno);
            pageinfo.setPageSize(pagesize);

            baseResp.setData(pageinfo);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.error("find mediaResouce list error mediaResource:{} pageno:{} pagesize:{} errorMsg:{}",mediaResource.toString(),pageno,pagesize,e);
        }
        return baseResp;
    }

    /**
     * 添加资源库
     * @param mediaResource
     * @return
     */
    @Override
    public BaseResp<Object> addMediaResource(MediaResource mediaResource) {
        logger.info("add mediaResource mediaResource",mediaResource.toString());
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            mediaResource.setCreatetime(new Date());
            int row = this.mediaResourceMapper.insertMediaResource(mediaResource);
            if(row > 0){
                //如果是PPT,则需要将PPT文档转成图片
                if(mediaResource.getFiletype() == 3){
                    final String filePath = mediaResource.getFilepath();
                    final String filename = mediaResource.getFilename();
                    final Integer mediaResourceId = mediaResource.getId();
//                    threadPoolTaskExecutor.execute(new Runnable() {
//                        @Override
//                        public void run() {
                            PPTToImage(filePath,filename,mediaResourceId);
//                            BaseResp<List<MediaResourceDetail>> baseResp1 = pptServiceApi.PPTToImage(filePath,filename,mediaResourceId);
//                            batchInsertMediaResourceDetail(baseResp1.getData());
//                        }
//                    });

                }
                baseResp.getExpandData().put("mediaResourceId",mediaResource.getId());
                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.error("add mediaResource mediaResource errorMsg:{}",mediaResource.toString(),e);
        }
        return baseResp;
    }

    public void batchInsertMediaResourceDetail(List<MediaResourceDetail> mediaResourceDetailList){
        if (null == mediaResourceDetailList){
            return;
        }
        this.mediaResourceDetailMapper.batchInsertMediaResourceDetail(mediaResourceDetailList);
    }

    /**
     * 获取资源库详情
     * @param id
     * @return
     */
    @Override
    public BaseResp<MediaResource> mediaResourceDetail(Integer id) {
        logger.info("get mediaResource detail id:{}",id);
        BaseResp<MediaResource> baseResp = new BaseResp<MediaResource>();
        try{
            MediaResource mediaResource = this.mediaResourceMapper.getMediaResourceDetail(id);
            if(mediaResource == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            baseResp.setData(mediaResource);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.error("get mediaResource detail id:{} errorMsg:{}",id,e);
        }
        return baseResp;
    }

    /**
     * 更新资源库文件
     * @param mediaResource
     * @return
     */
    @Override
    public BaseResp<Object> updateMediaResource(MediaResource mediaResource) {
        logger.info("update mediaResource mediaResource:{}",mediaResource.toString());
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            MediaResource oldMediaResource = this.mediaResourceMapper.getMediaResourceDetail(mediaResource.getId());
            int row = this.mediaResourceMapper.updateMediaResource(mediaResource);
            if(row > 0){
                if(mediaResource.getIsdel() == null || mediaResource.getIsdel() != 1){
                    if(oldMediaResource == null){
                        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
                    }
                    if(oldMediaResource.getResourcetypeid() == null && mediaResource.getResourcetypeid() != null){
                        int updateRow = this.mediaResourceTypeMapper.updateMediaResourceTypeCount(mediaResource.getResourcetypeid(),1);
                    }else if(oldMediaResource.getResourcetypeid() != null && mediaResource.getResourcetypeid() == null){
                        int updateRow = this.mediaResourceTypeMapper.updateMediaResourceTypeCount(oldMediaResource.getResourcetypeid(),-1);
                    }else if(!oldMediaResource.getResourcetypeid().equals(mediaResource.getResourcetypeid())){
                        int updateRow = this.mediaResourceTypeMapper.updateMediaResourceTypeCount(oldMediaResource.getResourcetypeid(),-1);
                        this.mediaResourceTypeMapper.updateMediaResourceTypeCount(mediaResource.getResourcetypeid(),1);
                    }
                }else{
                    int updateRow = this.mediaResourceTypeMapper.updateMediaResourceTypeCount(oldMediaResource.getResourcetypeid(),-1);
                }

                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.error("update mediaResource mediaResource:{} errorMsg:{}",mediaResource.toString(),e);
        }
        return baseResp;
    }

    /**
     * 转码通知 ,修改文件相关信息
     * @param key 文件的key
     * @param pickey 图片的地址
     * @param fileKey 文件路径
     * @param workflow 工作流 名称
     * @param duration 音频时长 只有音频才有时长
     * @param picAttribute
     * @return
     */
    @Override
    public BaseResp<Object> updateMediaResourceInfo(String key, String pickey,String fileKey, String workflow, String duration, String picAttribute) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            if(StringUtils.hasBlankParams(key,workflow)){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            String sourceKey = key;
            if(StringUtils.isBlank(duration)){
                duration = null;
            }
            if(workflow.contains("mp3")){
                sourceKey = "longbei_mp3/longbei_media_resource/"+key;
            }else{
                sourceKey = "longbei_media_resource_video/"+key;
            }
//            if(!workflow.contains("mp3") && StringUtils.isNotEmpty(pickey)){
//                pickey = "[\""+pickey+"\"]";
//            }

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("sourcePath",sourceKey);
            map.put("fileKey",fileKey);
            if(StringUtils.isNotEmpty(pickey)){
                map.put("picKey",pickey);
            }
            if(duration != null){
                map.put("duration",duration);
            }

            int row = this.mediaResourceMapper.updateMedia(map);
            if(row > 0){
                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.error("ali zhuanma updateMediaResourceInfo error key:{} fileKey:{} errorMsg:{}",key,fileKey,e);
        }
        return baseResp;
    }

    /**
     * 添加资源分类
     * @param typename
     * @param userid
     * @return
     */
    @Override
    public BaseResp<Object> addMediaResourceType(String typename, String userid) {
        logger.info("add mediaResourceType typename:{} userid:{}",typename,userid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            int count = this.mediaResourceTypeMapper.getUserMediaResourceTypeCount(userid);
            if(count >= MediaResourceType.userMaxMediaResourceTypeCount){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_1201,Constant.RTNINFO_SYS_1201);
            }
            MediaResourceType mediaResourceType = new MediaResourceType();
            mediaResourceType.setCreatetime(new Date());
            mediaResourceType.setIcount(0);
            mediaResourceType.setSort(0);
            mediaResourceType.setTypename(typename);
            mediaResourceType.setUserid(userid);
            int row = this.mediaResourceTypeMapper.addMediaResourceType(mediaResourceType);
            if(row > 0){
                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.error("add mediaResourceType error typename:{} userid:{} errorMsg:{}",typename,userid,e);
        }

        return baseResp;
    }

    /**
     * 更新媒体资源库分类
     * @param id
     * @param userid
     * @param typename
     * @return
     */
    @Override
    public BaseResp<Object> updateMediaResourceType(Integer id, String userid, String typename) {
        logger.info("update mediaResourceType id:{} typename:{} userid:{}",id,typename,userid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            MediaResourceType mediaResourceType = new MediaResourceType();
            mediaResourceType.setId(id);
            if(StringUtils.isNotEmpty(userid)){
                mediaResourceType.setUserid(userid);
            }
            mediaResourceType.setTypename(typename);
            int row = this.mediaResourceTypeMapper.updateMediaResourceType(mediaResourceType);
            if(row > 0){
                return baseResp.initCodeAndDesp();
            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,"您没有权限修改该分类!");
        }catch(Exception e){
            logger.error("update mediaResourceType id:{} typename:{} userid:{} errorMsg:{}",id,typename,userid,e);
        }
        return baseResp;
    }

    /**
     * 校验用户是否可以继续添加媒体资源分类
     * @param userid
     * @return
     */
    @Override
    public BaseResp<Object> checkUserAddMediaResourceType(String userid) {
        logger.info("check UserAddMediaResourceType userid:{}",userid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            int count = this.mediaResourceTypeMapper.getUserMediaResourceTypeCount(userid);
            if(count >= MediaResourceType.userMaxMediaResourceTypeCount){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_1201,Constant.RTNINFO_SYS_1201);
            }
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.info("check UserAddMediaResourceType userid:{} errorMsg:{}",userid,e);
        }
        return baseResp;
    }

    /**
     * 删除媒体资源库分类
     * @param id
     * @param userid
     * @return
     */
    @Override
    public BaseResp<Object> deleteMediaResourceType(Integer id, String userid) {
        logger.info("delete mediaResourceType id:{} userid:{}",id,userid);
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            int row = this.mediaResourceTypeMapper.deleteMediaResourceType(id,userid);
            if(row > 0){
                //将原该分类下的所有资源分类改成空
                this.mediaResourceMapper.updateMediaResourceTypeIsNull(id);
                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.error("delete mediaResourceType error id:{} userid:{} errorMsg:{}",id,userid,e);
        }
        return baseResp;
    }

    /**
     * 获取资源详情列表
     * @param mediaresourceid
     * @param userid
     * @return
     */
    @Override
    public BaseResp<List<String>> findMediaResourceDetailList(Integer mediaresourceid, Long userid) {
        logger.info("get mediaResource detail mediaresourceid:{} userid:{}",mediaresourceid,userid);
        BaseResp<List<String>> baseResp = new BaseResp<List<String>>();
        try{
            MediaResource mediaResource = this.mediaResourceMapper.getMediaResourceDetail(mediaresourceid);
            if(mediaResource == null || !userid.equals(mediaResource.getUserid())){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            List<String> mediaResourceDetailList = this.mediaResourceDetailMapper.findMediaResourceDetailList(mediaresourceid);
            baseResp.setData(mediaResourceDetailList);
            baseResp.initCodeAndDesp();

        }catch(Exception e){
            logger.info("get mediaResource detail mediaresourceid:{} userid:{} errorMsg:{}",mediaresourceid,userid,e);
        }
        return baseResp;
    }

    private boolean PPTToImage(String pptUrl,String filename,Integer mediaResourceId){
        String tempMediaResourcePath = getTempFilePath();

        Integer suffixIndex = filename.lastIndexOf(".");
        String realFilename = filename.substring(0,suffixIndex);
        //下载的ppt路径
        String pptFilePath = tempMediaResourcePath + filename;
        File pptFile = new File(pptFilePath);
        //PDF 文件名
        String outputFileString =tempMediaResourcePath+(new Date().getTime())+realFilename+".pdf";
        File outputFile = new File(outputFileString);

        //转成图片后的输出路径
        String imageOutput = tempMediaResourcePath+realFilename+(new Date().getTime())+"/";

        try{
            pptUrl = OssConfig.url+pptUrl;
            //1. 根据url 下载ppt
            if(!pptFile.getParentFile().exists()){
                pptFile.getParentFile().mkdirs();
            }
            logger.info("pptUrl = {}",pptUrl);
            boolean flag = downloadPPT(pptUrl,pptFilePath);
            logger.info("ppt download result:{}",flag);
            if(!flag){
                return false;
            }
            //2. 将下载的ppt 转成 pdf
            pptFile = new File(pptFilePath);
//            startService();
//            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);

            OpenOfficeConnection connection =
                    new SocketOpenOfficeConnection(AppserviceConfig.openoffice_addr,AppserviceConfig.openoffice_port);
            connection.connect();
            DocumentConverter documentConverter = new StreamOpenOfficeDocumentConverter(connection);

            documentConverter.convert(pptFile,outputFile);

            //3. 将pdf转成图片
            List<String> imageList = PDFToImage(outputFileString,imageOutput,realFilename);
            logger.info("ppttoImagelist imageList:{}",imageList);
//            List<String> imageList = PPTToImageUtil.doPPTtoImage(pptFile,imageOutput,realFilename,"png");

            //4.将所有图片 上传的到阿里云
            if(imageList == null || imageList.size() == 0){
                return false;
            }
            List<MediaResourceDetail> mediaResourceDetailList = new ArrayList<MediaResourceDetail>();
            for(int i=0;i<imageList.size();i++){
                File file = new File(imageList.get(i));
                String key = "longbei_media_resource/"+UUID.randomUUID().toString();
                InputStream in = new FileInputStream(file);
                ossService.putObject(OssConfig.bucketName,key, in);

                MediaResourceDetail mediaResourceDetail = new MediaResourceDetail();
                mediaResourceDetail.setSort(i);
                mediaResourceDetail.setMediaresourceid(mediaResourceId);
                mediaResourceDetail.setFilePath(OssConfig.url+key);
                mediaResourceDetailList.add(mediaResourceDetail);
            }

            //5.保存到数据库 mediaResourceDetail
            int row = this.mediaResourceDetailMapper.batchInsertMediaResourceDetail(mediaResourceDetailList);
            if(row > 0){
                return  true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //pptFilePath outputFileString imageOutput
            pptFile.deleteOnExit();
            outputFile.deleteOnExit();
            File tempFile = new File(imageOutput);
            tempFile.deleteOnExit();
        }
        return false;
    }

    private static List<String> PDFToImage(String sourceFile,String destFile,String filename) throws InterruptedException, IOException, PDFException, PDFSecurityException {
        ArrayList<String> imageList = new ArrayList<String>();
        Document document = null;
        BufferedImage img = null;
        float rotation = 0f;
        float zoom = 1.5f;

        //输出文件夹
        File file = new File(destFile);
        if (!file.exists()) {
            file.mkdirs();
        }
        document = new Document();
        document.setFile(sourceFile);
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            img = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN,
                    org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX,rotation,zoom);
            Iterator iter = ImageIO.getImageWritersBySuffix(FILETYPE_PNG);
            ImageWriter writer = (ImageWriter) iter.next();
            String imagePath =destFile+filename+"_"+(i+1)+".png";
            File outFile = new File(imagePath);
            FileOutputStream out = new FileOutputStream(outFile);
            ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
            writer.setOutput(outImage);
            writer.write(new IIOImage(img, null, null));
            imageList.add(imagePath);
        }
        img.flush();
        document.dispose();
        System.out.println("转码成功 ");
        return imageList;
    }

    // 启动服务
    public static void startService() {
        if(officeManager != null){
            return ;
        }
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        try {
            System.out.println("openOffice Manager start open....");
            configuration.setOfficeHome(getOfficeHome());// 设置OpenOffice.org安装目录
            configuration.setPortNumbers(8100); // 设置转换端口，默认为8100
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);// 设置任务执行超时为5分钟
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);// 设置任务队列超时为24小时

            officeManager = configuration.buildOfficeManager();

            officeManager.start();
            System.out.println("office Manager 启动成功!");
            System.out.println("************* success **************"+getOfficeHome());
        } catch (Exception ce) {
            System.out.println("************* fail **************"+getOfficeHome());
            System.out.println("office Manager 启动失败:" + ce);
        }
    }

    private boolean downloadPPT(String pptUrl,String pptFilePath) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        URL url = new URL(pptUrl);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(pptFilePath);

            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取OpenOffice安装目录
     * @return
     */
    public static String getOfficeHome() {
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return "/opt/openoffice4";
        } else if (Pattern.matches("Windows.*", osName)) {
            return "E:/software/OpenOffice 4";
        } else if (Pattern.matches("Mac.*", osName)) {
            return "/Applications/OpenOffice.app/Contents";
        }
        return null;
    }

    private String getTempFilePath(){
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return "/tmp/mediaResource/";
        } else if (Pattern.matches("Windows.*", osName)) {
            return "E:/MediaResourceTemp/";
        } else if (Pattern.matches("Mac.*", osName)) {
            return "/Users/smkk/Downloads/MediaResourceTemp/";
        }
        return null;
    }


    public static void stopService() {
        System.out.println("openOffice Manager 开始停止....");
        if (officeManager != null) {
            officeManager.stop();
        }
        System.out.println("openOffice Manager 停止成功!");
    }

    public static void initStopService(){
        try {
            System.out.println("openOffice Manager 开始关闭....");
            DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
            configuration.setOfficeHome(getOfficeHome());// 设置OpenOffice.org安装目录
            configuration.setPortNumbers(8100); // 设置转换端口，默认为8100
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);// 设置任务执行超时为5分钟
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);// 设置任务队列超时为24小时

            officeManager = configuration.buildOfficeManager();
            officeManager.stop();
            officeManager = null;
            System.out.println("office Manager 关闭成功!");
            System.out.println("************* success **************"+getOfficeHome());
        } catch (Exception ce) {
            System.out.println("************* fail **************"+getOfficeHome());
            System.out.println("office Manager 关闭失败:" + ce);
        }
    }
    /*************************** PPT转图片 end *************************/

}
