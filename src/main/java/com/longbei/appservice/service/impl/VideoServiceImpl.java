package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.VideoClassifyMapper;
import com.longbei.appservice.dao.VideoMapper;
import com.longbei.appservice.dao.redis.SpringJedisDao;
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
    @Autowired
    private SpringJedisDao springJedisDao;

    /**
     * 获取视频分类的列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Page<VideoClassify>> getVideoClassifyList(String keyword,Integer pageNo, Integer pageSize) {
        logger.info("get videoClassify list pageNo:{} pageSize:{}",pageNo,pageSize);
        BaseResp<Page<VideoClassify>> baseResp = new BaseResp<Page<VideoClassify>>();
        try{
            Integer startNum = (pageNo - 1) * pageSize;
            Map<String,Object> map = new HashMap<String,Object>();
            if(StringUtils.isNotEmpty(keyword)){
                map.put("keyword",keyword);
            }
            map.put("startNum",startNum);
            map.put("pageSize",pageSize);
            int count = this.videoClassifyMapper.getVideoClassifyCount(map);
            List<VideoClassify> list = new ArrayList<VideoClassify>();
            if(count > 0){
                list = this.videoClassifyMapper.getVideoClassifyList(map);
            }
            Page<VideoClassify> page = new Page<VideoClassify>();
            page.setTotalCount(count);
            page.setCurrentPage(pageNo);
            page.setPageSize(pageSize);
            page.setList(list);
            baseResp.setData(page);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.error("get videoClassify list pageNo:{} pageSize:{} errorMsg:{}",pageNo,pageSize,e);
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
    public BaseResp<Video> getVideo(Integer videoId,Boolean isShow) {
        logger.info("get video videoId:{}",videoId);
        BaseResp<Video> baseResp = new BaseResp<Video>();
        try{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("videoId",videoId);
            if(isShow != null){
                map.put("isShow",isShow);
            }
            Video video = this.videoMapper.getVideo(map);
            if(isShow != null){
                String likes = this.springJedisDao.get("video_"+videoId);
                video.setLikes(StringUtils.isNotEmpty(likes)?Integer.parseInt(likes):0);
            }
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

    /**
     * 根据视频分类的id 获取视频分类的信息和视频列表
     * @param videoClassifyId
     * @return
     */
    @Override
    public BaseResp<Object> getVideoListDetail(Integer videoClassifyId) {
        logger.info("get video list detail videoClassifyId:{}",videoClassifyId);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            VideoClassify videoClassify = this.videoClassifyMapper.getVideoClassify(videoClassifyId);
            if(videoClassify == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("videoClassifyId",videoClassifyId);
            map.put("orderType","sortnum");
            map.put("isShow",true);
            map.put("startNum",0);
            map.put("pageSize",6);
            List<Video> videoList = this.videoMapper.getVideoList(map);
            if(videoList == null){
                videoList = new ArrayList<Video>();
            }
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("videoList",videoList);
            resultMap.put("videoClassify",videoClassify);
            baseResp.setData(resultMap);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.info("get Video list detail videoClassifyId:{} errorMsg:{}",videoClassifyId,e);
        }
        return baseResp;
    }

    /**
     * 加载相关视频
     * @param videoId
     * @return
     */
    @Override
    public BaseResp<Object> loadRelevantVideo(Integer videoId) {
        logger.info("load relevant video videoId:{}",videoId);
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("videoId",videoId);
            Video video = this.videoMapper.getVideo(map);
            if(video == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            Map<String,Object> paraMap = new HashMap<>();
            paraMap.put("videoClassifyId",video.getVideoclassifyid());
            paraMap.put("sortnum",video.getSortnum());
            List<Video> videoList = this.videoMapper.getRelevantVideo(paraMap);
            if(videoList == null || videoList.size() == 0){
                paraMap.put("sortnum",-1);
                videoList = this.videoMapper.getRelevantVideo(paraMap);
            }
            baseResp.setData(videoList);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.info("load relevant video videoId:{} errorMsg;{}",videoId,e);
        }
        return baseResp;
    }

    /**
     * 点赞
     * @param videoId
     * @return
     */
    @Override
    public BaseResp<Object> addLike(Integer videoId) {
        this.springJedisDao.increment("video_"+videoId,(long)1);
        return new BaseResp<>().initCodeAndDesp();
    }

}
