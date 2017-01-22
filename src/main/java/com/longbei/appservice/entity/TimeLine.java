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


@Document(collection = "timeline")
public class TimeLine {

	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	private String userid;
	private String remark;
	@DBRef
	private TimeLineDetail contents;
	
	private Date createdate;

	private String messagetype; // 1 -- improve 2 --- rank 3 --- old award 4 -- new award
	
	private String ctype = "1";  //圈类型   0 龙杯默认全部公开进步圈  1 好友今不全  2 熟人圈 等等


	public String getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

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

	public TimeLineDetail getContents() {
		return contents;
	}

	public void setContents(TimeLineDetail contents) {
		this.contents = contents;
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
	
}
