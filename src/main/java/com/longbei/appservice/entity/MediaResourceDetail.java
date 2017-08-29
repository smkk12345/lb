package com.longbei.appservice.entity;

import java.io.Serializable;

/**
 * Created by wangyongzhi 17/8/11.
 */
public class MediaResourceDetail implements Serializable {
    private Integer id;
    private Integer mediaresourceid;//对应的资源库数据id
    private String filePath;//文件路径
    private Integer sort;//从0开始 0代表第一个

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMediaresourceid() {
        return mediaresourceid;
    }

    public void setMediaresourceid(Integer mediaresourceid) {
        this.mediaresourceid = mediaresourceid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
