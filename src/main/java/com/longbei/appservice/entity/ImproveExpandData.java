package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by smkk on 17/2/17.  Serializable
 */
public class ImproveExpandData implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<AppUserMongoEntity> appUserList;//点赞送花送钻用户列表 只取前5条
    private Integer lfdcount = 0;//点赞送花送钻 数量
    private String haslike = "0";//是否点赞
    private String hasflower = "0";//是否送花
    private String hasdiamond = "0";//时候送钻
    private String hascollect = "0";//是否收藏
    private SuperTopic superTopic;//超级话题

    public List<AppUserMongoEntity> getAppUserList() {
        return appUserList;
    }

    public void setAppUserList(List<AppUserMongoEntity> appUserList) {
        this.appUserList = appUserList;
    }

    public Integer getLfdcount() {
        return lfdcount;
    }

    public void setLfdcount(Integer lfdcount) {
        this.lfdcount = lfdcount;
    }

    public String getHaslike() {
        return haslike;
    }

    public void setHaslike(String haslike) {
        this.haslike = haslike;
    }

    public String getHasflower() {
        return hasflower;
    }

    public void setHasflower(String hasflower) {
        this.hasflower = hasflower;
    }

    public String getHasdiamond() {
        return hasdiamond;
    }

    public void setHasdiamond(String hasdiamond) {
        this.hasdiamond = hasdiamond;
    }

    public String getHascollect() {
        return hascollect;
    }

    public void setHascollect(String hascollect) {
        this.hascollect = hascollect;
    }

    public SuperTopic getSuperTopic() {
        return superTopic;
    }

    public void setSuperTopic(SuperTopic superTopic) {
        this.superTopic = superTopic;
    }
}
