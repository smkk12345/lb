package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.MediaResourceType;
import com.longbei.appservice.entity.MediaResource;
import com.longbei.appservice.service.MediaResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangyongzhi 17/7/27.
 */
@RestController
@RequestMapping(value="api/mediaResource")
public class MediaResourceApiController {

    @Autowired
    private MediaResourceService mediaResourceService;

    /**
     * 查询资源分类列表
     * @return
     */
    @RequestMapping(value="findMediaResourceTypeList")
    public BaseResp<List<MediaResourceType>> findMediaResourceTypeList(){
        return this.mediaResourceService.findMediaResourceTypeList();
    }

    /**
     * 查询资源列表
     * @param mediaResource 查询的条件
     *        注意mediaResource中的source来源 必传,标识是web查询还是admin查询
     *        source 0.web 1.admin 是哪个平台查询的 必传 如果来源是web,则userid必传
     *          istranscoding 是否只查询转码后的 1.代表只查询转码后的
     * @param pageno
     * @param pagesize
     * @return
     */
    @RequestMapping(value="findMediaResourceList")
    public BaseResp<Page<MediaResource>> findMediaResourceList(@RequestBody MediaResource mediaResource,String istranscoding, Integer pageno, Integer pagesize){
        if(pageno == null){
            pageno = 1;
        }
        if(pagesize == null){
            pagesize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        if(mediaResource == null || (mediaResource.getSource() != null && mediaResource.getSource() == 0 && mediaResource.getUserid() == null)){
            return new BaseResp<>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.mediaResourceService.findMediaResourceList(mediaResource,istranscoding,pageno,pagesize);
    }

    /**
     * 添加资源数据
     * @param mediaResource
     * @return
     */
    @RequestMapping(value="addMediaResource")
    public BaseResp<Object> addMediaResource(@RequestBody MediaResource mediaResource){
        if(mediaResource == null || StringUtils.isEmpty(mediaResource.getFilepath())){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.mediaResourceService.addMediaResource(mediaResource);
    }

    /**
     * 获取资源库文件详情
     * @param id
     * @return
     */
    @RequestMapping(value="mediaResourceDetail")
    public BaseResp<MediaResource> mediaResourceDetail(Integer id){
        if(id == null){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.mediaResourceService.mediaResourceDetail(id);
    }

    /**
     * 更新资源库文件
     * @param mediaResource
     * @return
     */
    @RequestMapping(value="updateMediaResource")
    public BaseResp<Object> updateMediaResource(@RequestBody MediaResource mediaResource){
        if(mediaResource == null || mediaResource.getId() == null){
            return new BaseResp<>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.mediaResourceService.updateMediaResource(mediaResource);
    }

}
