package com.longbei.appservice.entity;

import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by longbei on 2016/8/11.
 */
@Document(collection = "timelinenewinfo")
public class TimeLineNewInfo {
    @Id
    private String id;
    private String friendid;
    private String friendpic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getFriendpic() {
    		if(StringUtils.isBlank(friendpic)){
    			return "";
    		}
        return Constant.OSS_CDN+friendpic;
    }

    public void setFriendpic(String friendpic) {
        this.friendpic = friendpic;
    }
}
