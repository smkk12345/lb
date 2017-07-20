package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Video;
import com.longbei.appservice.entity.VideoClassify;
import com.longbei.appservice.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangyongzhi 17/6/28.
 */
@RequestMapping(value="api/video")
@RestController
public class VideoApiController {

    @Autowired
    private VideoService videoService;

    /**
     * 获取视频分类的列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value="getVideoClassifyList")
    public BaseResp<Page<VideoClassify>> getVideoClassifyList(String keyword,Integer pageNo, Integer pageSize){
        BaseResp<List<VideoClassify>> baseResp = new BaseResp<List<VideoClassify>>();
        if(pageNo == null){
            pageNo = 1;
        }
        if(pageSize == null){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        return this.videoService.getVideoClassifyList(keyword,pageNo,pageSize);
    }

    /**
     * 添加视频分类
     * @param title
     * @param brief
     * @param bannerurl
     * @param styleflag
     * @return
     */
    @RequestMapping(value="addVideoClassify")
    public BaseResp<Object> addVideoClassify(String title,String brief,String bannerurl,Integer styleflag){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(title,brief,bannerurl)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(styleflag == null){
            styleflag = 0;
        }
        return this.videoService.addVideoClassify(title,brief,bannerurl,styleflag);
    }

    /**
     * 获取视频分类
     * @param id
     * @return
     */
    @RequestMapping(value="getVideoClassify")
    public BaseResp<Object> getVideoClassify(Integer id){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(id == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.videoService.getVideoClassify(id);
    }

    /**
     * 删除视频分类
     * @param id
     * @return
     */
    @RequestMapping(value="deleteVideoClassify")
    public BaseResp<Object> deleteVideoClassify(Integer id){
        return this.videoService.deleteVideoClassify(id);
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
    @RequestMapping(value="editVideoClassify")
    public BaseResp<Object> editVideoClassify(Integer id,String title,String brief,String bannerurl,Integer styleflag){
        if(id == null || StringUtils.isEmpty(title) || StringUtils.isEmpty(brief) || StringUtils.isEmpty(bannerurl)){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.videoService.editVideoClassify(id,title,brief,bannerurl,styleflag);
    }

    /**
     * 获取视频列表
     * @param videoClassifyId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("getVideoList")
    public BaseResp<Page<Video>> getVideoList(Integer videoClassifyId,Integer pageNo,Integer pageSize){
        if(videoClassifyId == null){
            return new BaseResp<Page<Video>>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(pageNo == null){
            pageNo = 1;
        }
        if(pageSize == null){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        return this.videoService.getVideoList(videoClassifyId,pageNo,pageSize);
    }

    /**
     * 获取视频详情
     * @param videoId
     * @return
     */
    @RequestMapping(value="getVideo")
    public BaseResp<Video> getVideo(Integer videoId){
        if(videoId == null){
            return new BaseResp<Video>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.videoService.getVideo(videoId,null);
    }

    /**
     * 添加视频
     * @param title
     * @param brief
     * @param screenphoto
     * @param videourl
     * @param isshow
     * @param sortnum
     * @param videoClassifyId
     * @return
     */
    @RequestMapping(value="addVideo")
    public BaseResp<Object> addVideo(String title, String brief, String screenphoto, String videourl, Boolean isshow, Integer sortnum, Integer videoClassifyId){
        if(StringUtils.hasBlankParams(title,brief,screenphoto,videourl) || videoClassifyId == null){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(isshow == null){
            isshow = false;
        }
        if(sortnum == null){
            sortnum = 0;
        }
        return this.videoService.addVideo(title,brief,screenphoto,videourl,isshow,sortnum,videoClassifyId);
    }

    /**
     * 更新视频
     * @param id
     * @param title
     * @param brief
     * @param screenphoto
     * @param videourl
     * @param isshow
     * @param sortnum
     * @param videoClassifyId
     * @return
     */
    @RequestMapping(value="updateVideo")
    public BaseResp<Object> updateVideo(Integer id,String title, String brief, String screenphoto, String videourl, Boolean isshow, Integer sortnum, Integer videoClassifyId){
        if(StringUtils.hasBlankParams(title,brief,screenphoto,videourl) || videoClassifyId == null){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(isshow == null){
            isshow = false;
        }
        if(sortnum == null){
            sortnum = 0;
        }
        return this.videoService.updateVideo(id,title,brief,screenphoto,videourl,isshow,sortnum,videoClassifyId);
    }

    /**
     * 删除视频
     * @return
     */
    @RequestMapping(value="deleteVideo")
    public BaseResp<Object> deleteVideo(Integer id){
        if (id == null){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.videoService.deleteVideo(id);
    }

    /**
     * 获取视频详情信息和视频列表
     * @param videoClassifyId
     * @return
     */
    @RequestMapping(value="getVideoListDetail")
    public BaseResp<Object> getVideoListDetail(Integer videoClassifyId){
        if(videoClassifyId == null){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.videoService.getVideoListDetail(videoClassifyId);
    }

    /**
     * 获取视频详情
     * @param videoId
     * @return
     */
    @RequestMapping(value="getVideoDetail")
    public BaseResp<Video> getVideoDetail(Integer videoId){
        if(videoId == null){
            return new BaseResp<Video>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.videoService.getVideo(videoId,true);
    }

    /**
     * 加载相关视频
     * @param videoId
     * @return
     */
    @RequestMapping(value="loadRelevantVideo",produces = "application/json")
    public BaseResp<Object> loadRelevantVideo(Integer videoId){
        if(videoId == null){
            return new BaseResp<List<Video>>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.videoService.loadRelevantVideo(videoId);
    }

    /**
     * 点赞
     * @param videoId
     * @return
     */
    @RequestMapping(value="addLike",produces = "application/json")
    public BaseResp<Object> addLike(Integer videoId){
        if(videoId == null){
            return new BaseResp<List<Video>>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.videoService.addLike(videoId);
    }
}
