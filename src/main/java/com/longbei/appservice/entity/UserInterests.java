package com.longbei.appservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserInterests {
    private Integer id;

    private String userid;
    
	private String ptype;//十全十美  类型

    private String perfectname;//十全十美  名

    private Date createtime;

    private Date updatetime;
       
    public UserInterests(){
    }
    
    public UserInterests(String ptype,String perfectname){
        super();
        this.ptype = ptype;
        this.perfectname = perfectname;
    }
    
    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return userid 
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 
     * @param userid 
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 十全十美  类型
     * @return Ptype 十全十美  类型
     */
    public String getPtype() {
		return ptype;
	}
    /**
     * 十全十美  类型
     * @return Ptype 十全十美  类型
     */
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
    /**
     * 十全十美  名
     * @return perfectname 十全十美  名
     */
    public String getPerfectname() {
        return perfectname;
    }

    /**
     * 十全十美  名
     * @param perfectname 十全十美  名
     */
    public void setPerfectname(String perfectname) {
        this.perfectname = perfectname == null ? null : perfectname.trim();
    }

    /**
     * 
     * @return createtime 
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 
     * @param createtime 
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 
     * @return updatetime 
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 
     * @param updatetime 
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}