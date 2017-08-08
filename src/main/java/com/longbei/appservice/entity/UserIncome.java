package com.longbei.appservice.entity;

/**
 * 用户收益
 */
public class UserIncome {
    /**
     * id
     */
    private Integer id;

    private Long userid;//用户id

    private Integer total;//总龙币

    private Integer outgo;//支出龙币

    private Integer left;//剩余龙币

    private String updatetime;

    private String createtime;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public void setOutgo(Integer outgo) {
        this.outgo = outgo;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getId() {
        return id;
    }

    public Integer getLeft() {
        return left;
    }

    public Integer getOutgo() {
        return outgo;
    }

    public Integer getTotal() {
        return total;
    }

    public Long getUserid() {
        return userid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

}