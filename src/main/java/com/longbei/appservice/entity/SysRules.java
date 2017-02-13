package com.longbei.appservice.entity;

public class SysRules {
	
	private static final long serialVersionUID = 1L;
	private int addimprove = 10;//发进步

	private int maximprove;//一天最多发的进步数量
	private int maxrank;//一天最多参加的榜
	private int register;//注册＋龙分数
	private int checkin;//签到龙分数
	private int idcard;//身份证验证龙分数
	private int email;//邮箱验证龙分数
	private int invite;//邀请用户注册成功龙分数
	private int invitemoneyfirst; //邀请用户注册送进步币-一级
	private int invitemoneysecond; //邀请用户注册送进步币-二级
	private int invitemoneythird; //邀请用户注册送进步币-三级
	private int gorank;//进步上榜龙分数
	private int likeother;//给别人点赞自己加龙分
	private int limitlike;//点赞限制
	private int levellike;//每一级增加两个点赞数
	private int improvelike;//同一个进步每个用户只能点赞一次
	private int proof;//给别人证明  自己＋5
	private int donatermb;//捐赠一元钱 自己＋100龙分
	private int donatejinbu;//捐赠一个进步获取100龙分
	private int maxrate;//最大中奖概率
	private int minrate;//最小中奖概率
	private int rate;//默认中奖概率
	private int slevel1;//一级商家能发布的榜的人数
	private int slevel2;//二级商家能发布的榜的人数
	private int slevel3;//三级商家能发布的榜的人数
	private int slevel4;//四级商家能发布的榜的人数
	private int slevel5;//五级商家能发布的榜的人数
	private double star1;//无优惠
	private double star2;//95
	private double star3;//90
	private double star4;//85
	private double star5;//8
	private double nearrankdistance = 3;//周边的榜的距离

	private int liked;//	被点赞送积分数
	private int flowered;//被送花积分数


	public void setAddimprove(int addimprove) {
		this.addimprove = addimprove;
	}

	public int getAddimprove() {
		return addimprove;
	}

	public int getMaximprove() {
		return maximprove;
	}
	public void setMaximprove(int maximprove) {
		this.maximprove = maximprove;
	}
	public int getMaxrank() {
		return maxrank;
	}
	public void setMaxrank(int maxrank) {
		this.maxrank = maxrank;
	}

	public int getRegister() {
		return register;
	}
	public void setRegister(int register) {
		this.register = register;
	}
	public int getCheckin() {
		return checkin;
	}
	public void setCheckin(int checkin) {
		this.checkin = checkin;
	}
	public int getIdcard() {
		return idcard;
	}
	public void setIdcard(int idcard) {
		this.idcard = idcard;
	}
	public int getEmail() {
		return email;
	}
	public void setEmail(int email) {
		this.email = email;
	}
	public int getInvite() {
		return invite;
	}

	public int getInvitemoneyfirst() {
		return invitemoneyfirst;
	}

	public void setInvitemoneyfirst(int invitemoneyfirst) {
		this.invitemoneyfirst = invitemoneyfirst;
	}

	public int getInvitemoneysecond() {
		return invitemoneysecond;
	}

	public void setInvitemoneysecond(int invitemoneysecond) {
		this.invitemoneysecond = invitemoneysecond;
	}

	public int getInvitemoneythird() {
		return invitemoneythird;
	}

	public void setInvitemoneythird(int invitemoneythird) {
		this.invitemoneythird = invitemoneythird;
	}

	public void setInvite(int invite) {
		this.invite = invite;
	}

	public int getGorank() {
		return gorank;
	}
	public void setGorank(int gorank) {
		this.gorank = gorank;
	}
	public int getLikeother() {
		return likeother;
	}
	public void setLikeother(int likeother) {
		this.likeother = likeother;
	}
	public int getLimitlike() {
		return limitlike;
	}
	public void setLimitlike(int limitlike) {
		this.limitlike = limitlike;
	}
	public int getLevellike() {
		return levellike;
	}
	public void setLevellike(int levellike) {
		this.levellike = levellike;
	}
	public int getImprovelike() {
		return improvelike;
	}
	public void setImprovelike(int improvelike) {
		this.improvelike = improvelike;
	}
	public int getProof() {
		return proof;
	}
	public void setProof(int proof) {
		this.proof = proof;
	}
	public int getDonatermb() {
		return donatermb;
	}
	public void setDonatermb(int donatermb) {
		this.donatermb = donatermb;
	}
	public int getDonatejinbu() {
		return donatejinbu;
	}
	public void setDonatejinbu(int donatejinbu) {
		this.donatejinbu = donatejinbu;
	}
	public int getMaxrate() {
		return maxrate;
	}
	public void setMaxrate(int maxrate) {
		this.maxrate = maxrate;
	}
	public int getMinrate() {
		return minrate;
	}
	public void setMinrate(int minrate) {
		this.minrate = minrate;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getSlevel1() {
		return slevel1;
	}
	public void setSlevel1(int slevel1) {
		this.slevel1 = slevel1;
	}
	public int getSlevel2() {
		return slevel2;
	}
	public void setSlevel2(int slevel2) {
		this.slevel2 = slevel2;
	}
	public int getSlevel3() {
		return slevel3;
	}
	public void setSlevel3(int slevel3) {
		this.slevel3 = slevel3;
	}
	public int getSlevel4() {
		return slevel4;
	}
	public void setSlevel4(int slevel4) {
		this.slevel4 = slevel4;
	}
	public int getSlevel5() {
		return slevel5;
	}
	public void setSlevel5(int slevel5) {
		this.slevel5 = slevel5;
	}
	public double getStar1() {
		return star1;
	}
	public void setStar1(double star1) {
		this.star1 = star1;
	}
	public double getStar2() {
		return star2;
	}
	public void setStar2(double star2) {
		this.star2 = star2;
	}
	public double getStar3() {
		return star3;
	}
	public void setStar3(double star3) {
		this.star3 = star3;
	}
	public double getStar4() {
		return star4;
	}
	public void setStar4(double star4) {
		this.star4 = star4;
	}
	public double getStar5() {
		return star5;
	}
	public void setStar5(double star5) {
		this.star5 = star5;
	}

	public int getLiked() {
		return liked;
	}
	public void setLiked(int liked) {
		this.liked = liked;
	}
	public int getFlowered() {
		return flowered;
	}
	public void setFlowered(int flowered) {
		this.flowered = flowered;
	}
}
