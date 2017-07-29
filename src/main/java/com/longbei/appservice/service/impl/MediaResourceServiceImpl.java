package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.MediaResourceMapper;
import com.longbei.appservice.dao.MediaResourceTypeMapper;
import com.longbei.appservice.entity.MediaResource;
import com.longbei.appservice.entity.MediaResourceType;
import com.longbei.appservice.service.MediaResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangyongzhi 17/7/27.
 */
@Service("mediaResouceService")
public class MediaResourceServiceImpl implements MediaResourceService {
    private Logger logger = LoggerFactory.getLogger(MediaResourceServiceImpl.class);

    @Autowired
    private MediaResourceTypeMapper mediaResourceTypeMapper;
    @Autowired
    private MediaResourceMapper mediaResourceMapper;

    /**
     * 查询资源分类列表
     * @return
     */
    @Override
    public BaseResp<List<MediaResourceType>> findMediaResourceTypeList() {
        logger.info("find MediaResouceType List ");
        BaseResp<List<MediaResourceType>> baseResp = new BaseResp<List<MediaResourceType>>();
        try{
            List<MediaResourceType> mediaResourceTypeList = this.mediaResourceTypeMapper.findMediaResourceTypeList();
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
    public BaseResp<Page<MediaResource>> findMediaResourceList(MediaResource mediaResource, Integer pageno, Integer pagesize) {
        logger.info("find mediaResouce list mediaResource:{} pageno:{} pagesize:{}",mediaResource.toString(),pageno,pagesize);
        BaseResp<Page<MediaResource>> baseResp = new BaseResp<>();
        try{
            int count = 0;
            List<MediaResource> mediaResourceList= new ArrayList<>();
            count = this.mediaResourceMapper.findMediaResourceCount(mediaResource);
            if(count > 0){
                int startno = (pageno-1)*pagesize;
                mediaResourceList = this.mediaResourceMapper.findMediaResourceList(mediaResource,startno,pagesize);
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
                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.error("add mediaResource mediaResource errorMsg:{}",mediaResource.toString(),e);
        }
        return baseResp;
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
            int row = this.mediaResourceMapper.updateMediaResource(mediaResource);
            if(row > 0){
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
                sourceKey = "longbei_vido/longbei_media_resource/"+key;
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

}
