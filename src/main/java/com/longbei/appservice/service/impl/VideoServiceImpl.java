package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.VideoClassifyMapper;
import com.longbei.appservice.dao.VideoMapper;
import com.longbei.appservice.entity.Video;
import com.longbei.appservice.entity.VideoClassify;
import com.longbei.appservice.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangyongzhi 17/6/28.
 */
@Service("videoService")
public class VideoServiceImpl implements VideoService {
    private Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VideoClassifyMapper videoClassifyMapper;

    /**
     * 获取视频分类的列表
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<List<VideoClassify>> getVideoClassifyList(Integer startNum, Integer pageSize) {
        logger.info("get videoClassify list startNum:{} pageSize:{}",startNum,pageSize);
        BaseResp<List<VideoClassify>> baseResp = new BaseResp<List<VideoClassify>>();
        try{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("startNum",startNum);
            map.put("pageSize",pageSize);
            List<VideoClassify> list = this.videoClassifyMapper.getVideoClassifyList(map);
            baseResp.setData(list);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.error("get videoClassify list startNum:{} pageSize:{} errorMsg:{}",startNum,pageSize,e);
        }
        return baseResp;
    }

    /**
     * 添加视频分类
     * @param title
     * @param brief
     * @param bannerurl
     * @param styleflag
     * @return
     */
    @Override
    public BaseResp<Object> addVideoClassify(String title, String brief, String bannerurl, Integer styleflag) {
        logger.info("add videoClassify title:{} brief:{} bannerurl:{} styleflag:{}",title,brief,bannerurl,styleflag);
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            VideoClassify videoClassify = new VideoClassify();
            videoClassify.setTitle(title);
            videoClassify.setBrief(brief);
            videoClassify.setBannerurl(bannerurl);
            videoClassify.setStyleflag(styleflag);
            videoClassify.setCreatetime(new Date());

            int row = this.videoClassifyMapper.insertVideoClassify(videoClassify);
            if(row > 0){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch(Exception e){
            logger.error("add videoClassify title:{} brief:{} bannerurl:{} styleflag:{} errorMsg:{}",title,brief,bannerurl,styleflag,e);
        }
        return baseResp;
    }

    /**
     * 获取视频分类
     * @param id
     * @return
     */
    @Override
    public BaseResp<Object> getVideoClassify(Integer id) {
        logger.info("get videoClassify id:{}",id);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            VideoClassify videoClassify = this.videoClassifyMapper.getVideoClassify(id);
            baseResp.setData(videoClassify);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.info("get videoClassify id:{} errorMsg:{}",id,e);
        }

        return null;
    }

    /**
     * 删除视频分类
     * @param id
     * @return
     */
    @Override
    public BaseResp<Object> deleteVideoClassify(Integer id) {
        logger.info("delete videoClassify id:{}",id);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            int row = this.videoClassifyMapper.deleteVideoClassify(id);
            if(row > 0){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch(Exception e){
            logger.info("delete videoClassify id:{} errorMsg:{}",id,e);
        }
        return baseResp;
    }

    /**
     * 编辑视频分类
     * @param id
     * @param title
     * @param brief
     * @param bannerurl
     * @param styleflag
     * @return
     */
    @Override
    public BaseResp<Object> editVideoClassify(Integer id, String title, String brief, String bannerurl, Integer styleflag) {
        logger.info("edit videoClassify id:{} title:{} brief:{} bannerurl:{} styleflag:{}",id,title,brief,bannerurl,styleflag);
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            VideoClassify videoClassify = new VideoClassify();
            videoClassify.setId(id);
            videoClassify.setTitle(title);
            videoClassify.setBrief(brief);
            videoClassify.setBannerurl(bannerurl);
            videoClassify.setStyleflag(styleflag);

            int row = this.videoClassifyMapper.updateVideoClassify(videoClassify);
            if(row > 0){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch(Exception e){
            logger.error("edit videoClassify id:{} title:{} brief:{} bannerurl:{} styleflag:{} errorMsg:{}",id,title,brief,bannerurl,styleflag,e);
        }
        return baseResp;
    }

    /**
     * 获取视频列表
     * @param videoClassifyId
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<List<Video>> getVideoList(Integer videoClassifyId, Integer startNum, Integer pageSize) {
        logger.info("get video list videoClassifyId:{} startNum:{} pageSize:{}",videoClassifyId,startNum,pageSize);
        BaseResp<List<Video>> baseResp = new BaseResp<List<Video>>();
        try{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("videoClassifyId",videoClassifyId);
            map.put("startNum",startNum);
            map.put("pageSize",pageSize);
            List<Video> videoList = this.videoMapper.getVideoList(map);
            if(videoList != null){
                baseResp.setData(videoList);
            }else{
                baseResp.setData(new ArrayList<Video>());
            }
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.error("get video list videoClassifyId:{} startNum:{} pageSize:{} errorMsg:{}",videoClassifyId,startNum,pageSize,e);
        }

        return baseResp;
    }

    /**
     * 获取视频详情
     * @param videoId
     * @return
     */
    @Override
    public BaseResp<Video> getVideo(Integer videoId) {
        logger.info("get video videoId:{}",videoId);
        BaseResp<Video> baseResp = new BaseResp<Video>();
        try{
            Video video = this.videoMapper.getVideo(videoId);
            baseResp.setData(video);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.error("get video videoId:{} errorMsg:{}",videoId,e);
        }
        return baseResp;
    }

    /**
     * 添加视频
     * @param title
     * @param brief
     * @param screenpthoto
     * @param videourl
     * @param isshow
     * @param sortnum
     * @param videoClassifyId
     * @return
     */
    @Override
    public BaseResp<Object> addVideo(String title, String brief, String screenpthoto, String videourl, Boolean isshow, Integer sortnum, Integer videoClassifyId) {
        logger.info("add video title:{} brief:{} scrrenphoto:{} videourl:{} isshow:{} sortnum:{} videoClassifyId:{}",title,brief,screenpthoto,videourl,isshow,sortnum,videoClassifyId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Video video = new Video();
            video.setCreatetime(new Date());
            video.setTitle(title);
            video.setBrief(brief);
            video.setScreenpthoto(screenpthoto);
            video.setVideourl(videourl);
            video.setIsshow(isshow);
            video.setSortnum(sortnum);
            video.setVideoclassifyid(videoClassifyId);
            int row = this.videoMapper.addVideo(video);
            if(row > 0){
                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.info("add video title:{} brief:{} scrrenphoto:{} videourl:{} isshow:{} sortnum:{} videoClassifyId:{} errorMsg:{}",title,brief,screenpthoto,videourl,isshow,sortnum,videoClassifyId,e);
        }
        return baseResp;
    }

    /**
     * 更新视频
     * @param title
     * @param brief
     * @param screenpthoto
     * @param videourl
     * @param isshow
     * @param sortnum
     * @param videoClassifyId
     * @return
     */
    @Override
    public BaseResp<Object> updateVideo(Integer id,String title, String brief, String screenpthoto, String videourl, Boolean isshow, Integer sortnum, Integer videoClassifyId) {
        logger.info("update video id title:{} brief:{} scrrenphoto:{} videourl:{} isshow:{} sortnum:{} videoClassifyId:{}",title,brief,screenpthoto,videourl,isshow,sortnum,videoClassifyId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Video video = new Video();
            video.setId(id);
            video.setTitle(title);
            video.setBrief(brief);
            video.setScreenpthoto(screenpthoto);
            video.setVideourl(videourl);
            video.setIsshow(isshow);
            video.setSortnum(sortnum);
            int row = this.videoMapper.updateVideo(video);
            if(row > 0){
                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.info("update video id:{} title:{} brief:{} scrrenphoto:{} videourl:{} isshow:{} sortnum:{} videoClassifyId:{} errorMsg:{}",id,title,brief,screenpthoto,videourl,isshow,sortnum,videoClassifyId,e);
        }
        return baseResp;
    }

    /**
     * 删除视频
     * @param id
     * @return
     */
    @Override
    public BaseResp<Object> deleteVideo(Integer id) {
        logger.info("delete video id:{}",id);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            int row = this.videoMapper.deleteVideo(id);
            if(row > 0){
                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.error("delete video error id:{} errorMsg:{]",id,e);
        }

        return baseResp;
    }
}
