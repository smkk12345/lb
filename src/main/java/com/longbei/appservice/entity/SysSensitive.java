package com.longbei.appservice.entity;

/**
 * Created by lixb on 2017/3/22.
 */
public class SysSensitive {
    private String words;
    private Integer id;

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
}
