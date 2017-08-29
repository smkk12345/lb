package com.longbei.appservice.entity;

import java.io.Serializable;

/**
 * Created by lixb on 2017/3/22.
 */
public class SysSensitive implements Serializable {
    private String words;
    private Integer id;
    private String updatetime;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Integer getId() {
        return id;
    }

    public String getWords() {
        return words;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdatetime() {
        return updatetime;
    }
}
