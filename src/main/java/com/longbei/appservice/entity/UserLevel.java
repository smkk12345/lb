package com.longbei.appservice.entity;

public class UserLevel {
    private Long id;//编码

    private Integer grade;//等级名称

    private String star;//星级

    private Integer diff;//极差分值

    private Integer point;//等级分值

    private Double discount;//等级折扣

    private Integer pubrankjoinnum;//公共榜单最大人数
    private Integer pubranknum;//公共榜单数量
    private Integer circlenum;//圈子数量
    private Integer prirankjoinnum;//私有榜单最大人数
    private Integer priranknum;//私有榜单数量
    private Integer classroomnum;//教室数量
    private Integer joinranknum;//能参与榜单的数量
    private Integer likes;//每天点赞数
    private Integer updatetime;//更新时间

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setCirclenum(Integer circlenum) {
        this.circlenum = circlenum;
    }

    public void setClassroomnum(Integer classroomnum) {
        this.classroomnum = classroomnum;
    }

    public void setJoinranknum(Integer joinranknum) {
        this.joinranknum = joinranknum;
    }

    public void setPrirankjoinnum(Integer prirankjoinnum) {
        this.prirankjoinnum = prirankjoinnum;
    }

    public void setPriranknum(Integer priranknum) {
        this.priranknum = priranknum;
    }

    public void setPubrankjoinnum(Integer pubrankjoinnum) {
        this.pubrankjoinnum = pubrankjoinnum;
    }

    public void setPubranknum(Integer pubranknum) {
        this.pubranknum = pubranknum;
    }

    public void setUpdatetime(Integer updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getCirclenum() {
        return circlenum;
    }

    public Integer getClassroomnum() {
        return classroomnum;
    }

    public Integer getJoinranknum() {
        return joinranknum;
    }

    public Integer getPrirankjoinnum() {
        return prirankjoinnum;
    }

    public Integer getPriranknum() {
        return priranknum;
    }

    public Integer getPubrankjoinnum() {
        return pubrankjoinnum;
    }

    public Integer getPubranknum() {
        return pubranknum;
    }

    public Integer getUpdatetime() {
        return updatetime;
    }


    /**
     * 编码
     * @return id 编码
     */
    public Long getId() {
        return id;
    }

    /**
     * 编码
     * @param id 编码
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 等级名称
     * @return grade 等级名称
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * 等级名称
     * @param grade 等级名称
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * 星级
     * @return star 星级
     */
    public String getStar() {
        return star;
    }

    /**
     * 星级
     * @param star 星级
     */
    public void setStar(String star) {
        this.star = star == null ? null : star.trim();
    }

    /**
     * 极差分值
     * @return diff 极差分值
     */
    public Integer getDiff() {
        return diff;
    }

    /**
     * 极差分值
     * @param diff 极差分值
     */
    public void setDiff(Integer diff) {
        this.diff = diff;
    }

    /**
     * 等级分值
     * @return point 等级分值
     */
    public Integer getPoint() {
        return point;
    }

    /**
     * 等级分值
     * @param point 等级分值
     */
    public void setPoint(Integer point) {
        this.point = point;
    }

    /**
     * 等级折扣
     * @return discount 等级折扣
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * 等级折扣
     * @param discount 等级折扣
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}