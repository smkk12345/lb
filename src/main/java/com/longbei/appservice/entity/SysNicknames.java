package com.longbei.appservice.entity;

import java.io.Serializable;

/**
 * Created by lixb on 2017/8/4.
 */
public class SysNicknames implements Serializable {

    private Integer id;
    private String nickname;

    public Integer getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
