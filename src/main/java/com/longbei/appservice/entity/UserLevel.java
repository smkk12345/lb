package com.longbei.appservice.entity;

public class UserLevel {
    private Long id;//编码

    private Integer grade;//等级名称

    private String star;//星级

    private Integer diff;//极差分值

    private Integer point;//等级分值

    private Double discount;//等级折扣

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