package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.Transient;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class TimeLine implements Cloneable{

	@Id
	private String id;
	private String userid;
	private String remark;
	@DBRef
	private TimeLineDetail timeLineDetail;
	private String messagetype; // 1 -- improve 2 --- rank 3 --- old award 4 -- new award
//	private String ctype = "1";  //0--广场 1--我的 2--好友，关注，熟人 3-好友 4-关注 5-熟人
	private String ptype; //十全十美
	private String businesstype;//微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
	private Long businessid;
	private Date createdate;
	private int ispublic;

	public int getIspublic() {
		return ispublic;
	}

	public void setIspublic(int ispublic) {
		this.ispublic = ispublic;
	}

	public void setBusinessid(Long businessid) {
		this.businessid = businessid;
	}

	public Long getBusinessid() {
		return businessid;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

//	public String getCtype() {
//		return ctype;
//	}
//
//	public void setCtype(String ctype) {
//		this.ctype = ctype;
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public TimeLineDetail getTimeLineDetail() {
		return timeLineDetail;
	}

	public void setTimeLineDetail(TimeLineDetail timeLineDetail) {
		this.timeLineDetail = timeLineDetail;
	}

	public String getCreatedate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beijingcal = Calendar.getInstance();
        beijingcal.setTimeInMillis(createdate.getTime());
        return sf.format(beijingcal.getTime());
	}

	public void setCreatedate(Date createdate) {
//		if(!StringUtils.isBlank(createdate)){
//			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			try {
//				this.createdate = sf.format(sf.parse(createdate));
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}else{
			this.createdate = createdate;
//		}
	}

	@Transient
	@JsonInclude(Include.ALWAYS)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public TimeLine clone() {
		TimeLine timeLine1 = null;
		try{
			timeLine1 = (TimeLine)super.clone();
		}catch(CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return timeLine1;
	}
}
