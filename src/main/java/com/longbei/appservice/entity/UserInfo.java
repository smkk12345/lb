package com.longbei.appservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserInfo {
    private Integer id;

    private Long userid;//用户id

    private Integer totalflower;//花

    private Integer totaldiamond;//钻石

    private Integer totalmoney;//龙币

    private Integer totalcoin;//进步币

    private Integer totalimp;//总进步数
    private Integer totallikes;//总赞
    private Integer totalfans;//总粉丝数

    private String nickname;//昵称

    private String realname;//真名

    private String sex;//性别  0 男  1 女

    private String city;//所在城市


	private String area;//所在区域

    private String brief;//个人简介

    private Date birthday;//生日

    private String constellation;//星座

    private String blood;//血型

    private String feeling;//感情状态

    private Date updatetime;//更新时间

    private Date screatetime;

    private Date createtime;

    private Date ecreatetime;

    private String payword;//支付密码

    private String avatar;//头像

    private String username;//冗余手机号

    private Long rcuserid;//推荐人id

    private String invitecode;//邀请人ids

    private Long inviteuserid;//邀请人id

    private String rytoken;//融云token

    private String deviceindex;//手机设备号

    private String devicetype;//手机类型

    private String hcnickname;//是否修改了用户昵称  0 未修改  1 已经修改

    private String islike = "0";//是否关注   0：未关注   1：已关注

    private String isfriend = "0"; //是否是好友    0：不是   1：是

    private Integer point;//龙分

    private Integer sgrade; //查询用

    private Integer grade;//等级

    private Integer egrade; //查询用

    private Integer curpoint;//当前龙分

    private String schoolcertify; //学历认证

    private String jobcertify; //工作认证

    private String isfashionman; //达人

    private Date upfashionmantime; //设置为达人时间

    private Date downfashionmantime; //取消达人时间

    private Integer sort; //排序

    private String settings; //设置

    public String getJobcertify() {
        return jobcertify;
    }

    public void setJobcertify(String jobcertify) {
        this.jobcertify = jobcertify;
    }

    public String getSchoolcertify() {
        return schoolcertify;
    }

    public void setSchoolcertify(String schoolcertify) {
        this.schoolcertify = schoolcertify;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getIsfashionman() {
        return isfashionman;
    }

    public void setIsfashionman(String isfashionman) {
        this.isfashionman = isfashionman;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpfashionmantime() {
        return upfashionmantime;
    }

    public void setUpfashionmantime(Date upfashionmantime) {
        this.upfashionmantime = upfashionmantime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getDownfashionmantime() {
        return downfashionmantime;
    }

    public void setDownfashionmantime(Date downfashionmantime) {
        this.downfashionmantime = downfashionmantime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    private List<UserJob> jobList = new ArrayList<UserJob>();

    private List<UserSchool> schoolList = new ArrayList<UserSchool>();

    private List<UserInterests> interestList = new ArrayList<UserInterests>();

    private List<UserPlDetail> detailList = new ArrayList<UserPlDetail>();  //用户十全十美的信息列表

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getScreatetime() {
        return screatetime;
    }

    public void setScreatetime(Date screatetime) {
        this.screatetime = screatetime;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getEcreatetime() {
        return ecreatetime;
    }

    public void setEcreatetime(Date ecreatetime) {
        this.ecreatetime = ecreatetime;
    }

    public Integer getSgrade() {
        return sgrade;
    }

    public void setSgrade(Integer sgrade) {
        this.sgrade = sgrade;
    }

    public Integer getEgrade() {
        return egrade;
    }

    public void setEgrade(Integer egrade) {
        this.egrade = egrade;
    }

    public UserInfo(){}
    public UserInfo(long userid,String nickname,String avatar,String sex){
    		super();
    		this.userid = userid;
    		this.nickname = nickname;
    		this.avatar = avatar;
    		this.sex = sex;
    }

    public void setCurpoint(Integer curpoint) {
        this.curpoint = curpoint;
    }

    public Integer getCurpoint() {
        return curpoint;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getGrade() {
        return grade;
    }

    public Integer getPoint() {
        return point;
    }

    /**
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    public void setTotalfans(Integer totalfans) {
        this.totalfans = totalfans;
    }

    public void setTotalimp(Integer totalimp) {
        this.totalimp = totalimp;
    }

    public void setTotallikes(Integer totallikes) {
        this.totallikes = totallikes;
    }

    public Integer getTotalfans() {
        return totalfans;
    }

    public Integer getTotalimp() {
        return totalimp;
    }

    public Integer getTotallikes() {
        return totallikes;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 用户id
     * @return userid 用户id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 花
     * @return totalflower 花
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getTotalflower() {
        return totalflower;
    }

    /**
     * 花
     * @param totalflower 花
     */
    public void setTotalflower(Integer totalflower) {
        this.totalflower = totalflower;
    }

    /**
     * 钻石
     * @return totaldiamond 钻石
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getTotaldiamond() {
        return totaldiamond;
    }

    /**
     * 钻石
     * @param totaldiamond 钻石
     */
    public void setTotaldiamond(Integer totaldiamond) {
        this.totaldiamond = totaldiamond;
    }

    /**
     * 龙币
     * @return totalmoney 龙币
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getTotalmoney() {
        return totalmoney;
    }

    /**
     * 龙币
     * @param totalmoney 龙币
     */
    public void setTotalmoney(Integer totalmoney) {
        this.totalmoney = totalmoney;
    }

    /**
     * 进步币
     * @return totalcoin 进步币
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getTotalcoin() {
        return totalcoin;
    }

    /**
     * 进步币
     * @param totalcoin 进步币
     */
    public void setTotalcoin(Integer totalcoin) {
        this.totalcoin = totalcoin;
    }

    /**
     * 昵称
     * @return nickname 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 昵称
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * 真名
     * @return realname 真名
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 真名
     * @param realname 真名
     */
    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    /**
     * 性别  0 男  1 女
     * @return sex 性别  0 男  1 女
     */
    @JsonInclude(Include.ALWAYS)
    public String getSex() {
        return sex;
    }

    /**
     * 性别  0 男  1 女
     * @param sex 性别  0 男  1 女
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * 所在城市
     * @return city 所在城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 所在城市
     * @param city 所在城市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }
    /**
     * 所在区域
     * @param city 所在区域
     */
    public String getArea() {
		return area;
	}
    /**
     * 所在区域
     * @param city 所在区域
     */
	public void setArea(String area) {
		this.area = area;
	}
    /**
     * 个人简介
     * @return brief 个人简介
     */
    public String getBrief() {
        return brief;
    }

    /**
     * 个人简介
     * @param brief 个人简介
     */
    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }

    /**
     * 生日
     * @return birthday 生日
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 生日
     * @param birthday 生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 星座
     * @return constellation 星座
     */
    public String getConstellation() {
        return constellation;
    }

    /**
     * 星座
     * @param constellation 星座
     */
    public void setConstellation(String constellation) {
        this.constellation = constellation == null ? null : constellation.trim();
    }

    /**
     * 血型
     * @return blood 血型
     */
    @JsonInclude(Include.ALWAYS)
    public String getBlood() {
        return blood;
    }

    /**
     * 血型
     * @param blood 血型
     */
    public void setBlood(String blood) {
        this.blood = blood == null ? null : blood.trim();
    }

    /**
     * 感情状态
     * @return feeling 感情状态
     */
    @JsonInclude(Include.ALWAYS)
    public String getFeeling() {
        return feeling;
    }

    /**
     * 感情状态
     * @param feeling 感情状态
     */
    public void setFeeling(String feeling) {
        this.feeling = feeling == null ? null : feeling.trim();
    }

    /**
     * 更新时间
     * @return updatetime 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 
     * @return createtime 
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
     * 支付密码
     * @return payword 支付密码
     */
    @JsonInclude(Include.ALWAYS)
    public String getPayword() {
        return payword;
    }

    /**
     * 支付密码
     * @param payword 支付密码
     */
    public void setPayword(String payword) {
        this.payword = payword == null ? null : payword.trim();
    }

    /**
     * 头像
     * @return avatar 头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 头像
     * @param avatar 头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    /**
     * 冗余手机号
     * @return username 冗余手机号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 冗余手机号
     * @param username 冗余手机号
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 推荐人id
     * @return rcuserid 推荐人id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getRcuserid() {
        return rcuserid;
    }

    /**
     * 推荐人id
     * @param rcuserid 推荐人id
     */
    public void setRcuserid(Long rcuserid) {
        this.rcuserid = rcuserid;
    }

    /**
     * 邀请人ids
     * @return invitecode 邀请人ids
     */
    @JsonInclude(Include.ALWAYS)
    public String getInvitecode() {
        return invitecode;
    }

    /**
     * 邀请人ids
     * @param invitecode 邀请人ids
     */
    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode == null ? null : invitecode.trim();
    }

    /**
     * 邀请人id
     * @return inviteuserid 邀请人id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getInviteuserid() {
        return inviteuserid;
    }

    /**
     * 邀请人id
     * @param inviteuserid 邀请人id
     */
    public void setInviteuserid(Long inviteuserid) {
        this.inviteuserid = inviteuserid;
    }


   
	public String getDeviceindex() {
		return deviceindex;
	}

	public void setDeviceindex(String deviceindex) {
		this.deviceindex = deviceindex;
	}

	@JsonInclude(Include.ALWAYS)
	public String getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	
	@JsonInclude(Include.ALWAYS)
	public String getHcnickname() {
		return hcnickname;
	}
	public void setHcnickname(String hcnickname) {
		this.hcnickname = hcnickname;
	}
	
	@JsonInclude(Include.ALWAYS)
	public String getRytoken() {
		return rytoken;
	}
	public void setRytoken(String rytoken) {
		this.rytoken = rytoken;
	}
	
	@JsonInclude(Include.ALWAYS)
	public String getIslike() {
		return islike;
	}
	
	public void setIslike(String islike) {
		this.islike = islike;
	}
	
	@JsonInclude(Include.ALWAYS)
	public String getIsfriend() {
		return isfriend;
	}
	public void setIsfriend(String isfriend) {
		this.isfriend = isfriend;
	}
	public List<UserJob> getJobList() {
		return jobList;
	}
	public void setJobList(List<UserJob> jobList) {
		this.jobList = jobList;
	}
	public List<UserSchool> getSchoolList() {
		return schoolList;
	}
	public void setSchoolList(List<UserSchool> schoolList) {
		this.schoolList = schoolList;
	}
	public List<UserInterests> getInterestList() {
		return interestList;
	}
	public void setInterestList(List<UserInterests> interestList) {
		this.interestList = interestList;
	}
	public List<UserPlDetail> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<UserPlDetail> detailList) {
		this.detailList = detailList;
	}
}