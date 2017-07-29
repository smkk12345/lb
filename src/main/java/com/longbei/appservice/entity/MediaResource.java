package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by wangyongzhi 17/7/27.
 */
public class MediaResource {

    private Integer id;
    private Integer resourcetypeid;//所属分类的id
    private String title;//资源名称
    private Date createtime;//创建时间
    private String filename;//原始文件名称
    private Integer filesize;//文件大小 单位是M
    private String screenpthoto;//视频截图地址
    private String filepath;//文件路径
    private Long userid;//所属用户id
    private Integer ispublic;//是否公开 0.隐私 1.公开
    private Integer source;//来源 0.web上传 1.admin上传
    private String filesuffix;//文件后缀
    private Integer filetype;//文件类型 0.视频 1.音频 2.其他文件 3.PPT 4.图片 5.doc 6.ppt 7.excel
    private Integer isdel;//是否已经删除 0.未删除 1.已删除

    private String filekey;//文件的路径
    private String duration;//音频时长

    private String resourcetypename;//所属分类的名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourcetypeid() {
        return resourcetypeid;
    }

    public void setResourcetypeid(Integer resourcetypeid) {
        this.resourcetypeid = resourcetypeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public String getScreenpthoto() {
        return screenpthoto;
    }

    public void setScreenpthoto(String screenpthoto) {
        this.screenpthoto = screenpthoto;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getFilesuffix() {
        return filesuffix;
    }

    public void setFilesuffix(String filesuffix) {
        this.filesuffix = filesuffix;
    }

    public Integer getFiletype() {
        return filetype;
    }

    public void setFiletype(Integer filetype) {
        this.filetype = filetype;
    }

    public String getResourcetypename() {
        return resourcetypename;
    }

    public void setResourcetypename(String resourcetypename) {
        this.resourcetypename = resourcetypename;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getFilekey() {
        return filekey;
    }

    public void setFilekey(String filekey) {
        this.filekey = filekey;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "MediaResource{" +
                "id=" + id +
                ", resourcetypeid=" + resourcetypeid +
                ", title='" + title + '\'' +
                ", createtime=" + createtime +
                ", filename='" + filename + '\'' +
                ", filesize=" + filesize +
                ", screenpthoto='" + screenpthoto + '\'' +
                ", filepath='" + filepath + '\'' +
                ", userid=" + userid +
                ", ispublic=" + ispublic +
                ", source=" + source +
                ", filesuffix='" + filesuffix + '\'' +
                ", filetype=" + filetype +
                ", isdel=" + isdel +
                ", filekey='" + filekey + '\'' +
                ", duration='" + duration + '\'' +
                ", resourcetypename='" + resourcetypename + '\'' +
                '}';
    }
}
