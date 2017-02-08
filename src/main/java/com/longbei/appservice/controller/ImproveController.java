package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.ImpComplaints;
import com.longbei.appservice.entity.ImproveCircle;
import com.longbei.appservice.entity.ImproveClassroom;
import com.longbei.appservice.entity.ImproveGoal;
import com.longbei.appservice.entity.ImproveRank;
import com.longbei.appservice.service.ImpComplaintsService;
import com.longbei.appservice.service.ImproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * 进步操作
 *
 * @author luye
 * @create 2017-01-19 上午11:11
 **/
@Controller
@RequestMapping(value = "improve")
public class ImproveController {

	private static Logger logger = LoggerFactory.getLogger(ImproveController.class);

	@Autowired
	private ImproveService improveService;

	@Autowired
	private ImpComplaintsService impComplaintsService;

	/**
	 * @Title: http://ip:port/app_service/improve/addImpComplaints
	 * @Description: 投诉进步
	 * @param @param userid
	 * @param @param impid 进步id
	 * @param @param impid 投诉内容
	 * @param @param contenttype  0：该微进步与龙榜内容不符~~ 
	 * @param @param gtype 0 零散 1 目标中 2 榜中 3 圈中 4教室中
	 * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
	 * @auther yinxc
	 * @currentdate:2017年2月7日
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "addImpComplaints")
	public BaseResp<Object> addImpComplaints(String userid, String impid, String content, String contenttype,
			String gtype) {
		logger.info("addImpComplaints userid={},impid={},content={},contenttype={},gtype={}", userid, impid, content,
				contenttype, gtype);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid, impid, contenttype, gtype)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			ImpComplaints record = new ImpComplaints();
			record.setContent(content);
			record.setContenttype(contenttype);
			record.setCreatetime(new Date());
			record.setGtype(gtype);
			record.setImpid(Long.parseLong(impid));
			record.setStatus("0");
			record.setUserid(Long.parseLong(userid));
			baseResp = impComplaintsService.insertSelective(record);
		} catch (Exception e) {
			logger.error(
					"addImpComplaints userid = {}, impid = {}, content = {}, contenttype = {}, gtype = {}, msg = {}",
					userid, impid, content, contenttype, gtype, e);
		}
		return baseResp;

	}

	/**
	 * 发布进步
	 * 
	 * @param userid
	 *            用户id
	 * @param brief
	 *            说明
	 * @param pickey
	 *            图片的key
	 * @param filekey
	 *            文件key 视频文件 音频文件 普通文件
	 * @param businesstype
	 *            微进步关联的业务类型 0 未关联 1 目标 2 榜 3 圈子 4教室
	 * @param ptype
	 *            十全十美id
	 * @param ispublic
	 *            可见程度 0 私密 1 好友可见 2 全部可见
	 * @param itype
	 *            类型 0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public BaseResp<Object> insertImprove(String userid, String brief, String pickey, String filekey,
			String businesstype, String businessid, String ptype, String ispublic, String itype) {
		logger.info(
				"insertImprove brief:{}," + "pickey:{},filekey:{},businesstype:{},ptype:{}," + "ispublic:{},itype:{}",
				brief, pickey, filekey, businesstype, ptype, ispublic, itype);
		if (StringUtils.hasBlankParams(userid, businesstype, ptype, ispublic, itype)) {
			return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		if (StringUtils.isBlank(brief) && StringUtils.isBlank(pickey) && StringUtils.isBlank(filekey)) {
			return new BaseResp(Constant.STATUS_SYS_40, Constant.RTNINFO_SYS_40);
		}
		boolean flag = false;
		try {
			flag = improveService.insertImprove(userid, brief, pickey, filekey, businesstype, businessid, ptype,
					ispublic, itype);
			if (flag) {
				logger.debug("insert improve success");
				return BaseResp.ok(Constant.RTNINFO_SYS_41);
			}
		} catch (Exception e) {
			logger.error("insert improve is error:{}", e);
		}
		logger.info("insert improve fail");
		return new BaseResp(Constant.STATUS_SYS_42, Constant.RTNINFO_SYS_42);
	}

	/**
	 * 删除进步
	 * 
	 * @param userid
	 *            用户id
	 * @param improveid
	 *            进步id
	 * @param businesstype
	 *            进步所属业务类型 如 榜，教室等
	 * @param businessid
	 *            所属业务类型id 如 榜id 教室id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public BaseResp<Object> removeImprove(String userid, String improveid, String businesstype, String businessid) {
		logger.debug("remove improve userid:{} improveid:{} businesstype:{} businessid:{}", userid, improveid,
				businesstype, businessid);
		if (StringUtils.hasBlankParams(userid, improveid, businesstype, businessid)) {
			return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		boolean flag = false;
		try {
			flag = improveService.removeImprove(userid, improveid, businesstype, businessid);
			if (flag) {
				logger.debug("remove improve success");
				return BaseResp.ok(Constant.RTNINFO_SYS_41);
			}
		} catch (Exception e) {
			logger.error("remove improve is error:{}", e);
		}
		logger.info("remove improve fail");
		return new BaseResp(Constant.STATUS_SYS_42, Constant.RTNINFO_SYS_42);
	}

	/**
	 * url: http://ip:port/app_service/improve/rank/list method: POST 获取进步列表(榜中)
	 * 
	 * @param userid
	 *            用户id
	 * @param rankid
	 *            榜单id
	 * @param sorttype
	 *            排序类型（ 0 - 默认 1 - 动态 2 - 时间）
	 * @param startNo
	 *            开始条数
	 * @param pageSize
	 *            页面显示条数
	 * @return
	 * @author:luye
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "rank/list", method = RequestMethod.POST)
	public BaseResp selectRankImproveList(String userid, String rankid, String sorttype, String sift, String startNo,
			String pageSize) {
		if (StringUtils.hasBlankParams(userid, rankid, sorttype, sift)) {
			return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		if (StringUtils.isBlank(startNo)) {
			startNo = Constant.DEFAULT_START_NO;
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = Constant.DEFAULT_PAGE_SIZE;
		}
		List<ImproveRank> improves = null;
		try {
			if ("1".equals(sorttype)) {
				improves = improveService.selectRankImproveList(userid, rankid, sift, Integer.parseInt(startNo),
						Integer.parseInt(pageSize));

			} else {
				improves = improveService.selectRankImproveListByDate(userid, rankid, sift, Integer.parseInt(startNo),
						Integer.parseInt(pageSize));
			}
		} catch (Exception e) {
			logger.error("select rank improve list is error:{}", e);
		}
		if (null == improves) {
			return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
		}
		BaseResp<List<ImproveRank>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
		baseres.setData(improves);
		return baseres;
	}

	/**
	 * 获取进步列表（圈中）
	 * 
	 * @param userid
	 *            用户id
	 * @param circleid
	 *            圈子中id
	 * @param sorttype
	 *            排序类型 0 - 默认 1 - 动态 2 - 时间）
	 * @param startNo
	 *            分页开始条数
	 * @param pageSize
	 *            分页每页显示条数
	 * @return
	 * @author:luye
	 * @date 2017/2/4
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "circle/list", method = RequestMethod.POST)
	public BaseResp selectCircleImproveList(String userid, String circleid, String sorttype, String sift,
			String startNo, String pageSize) {
		if (StringUtils.hasBlankParams(userid, circleid, sorttype, sift)) {
			return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		if (StringUtils.isBlank(startNo)) {
			startNo = Constant.DEFAULT_START_NO;
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = Constant.DEFAULT_PAGE_SIZE;
		}
		List<ImproveCircle> improves = null;
		try {
			if ("1".equals(sorttype)) {
				improves = improveService.selectCircleImproveList(userid, circleid, sift, Integer.parseInt(startNo),
						Integer.parseInt(pageSize));

			} else {
				improves = improveService.selectCircleImproveListByDate(userid, circleid, sift,
						Integer.parseInt(startNo), Integer.parseInt(pageSize));
			}
		} catch (Exception e) {
			logger.error("select circle improve list is error:{}", e);
		}
		if (null == improves) {
			return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
		}
		BaseResp<List<ImproveCircle>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
		baseres.setData(improves);
		return baseres;
	}

	/**
	 * 获取教室中进步列表
	 * 
	 * @param userid
	 *            用户id
	 * @param classroomid
	 *            教室id
	 * @param sorttype
	 *            排序类型
	 * @param startNo
	 *            分页开始条数
	 * @param pageSize
	 *            分页每页显示条数
	 * @return
	 * @author:luye
	 * @date 2017/2/4
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "classroom/list", method = RequestMethod.POST)
	public BaseResp selectClassroomImproveList(String userid, String classroomid, String sorttype, String sift,
			String startNo, String pageSize) {
		if (StringUtils.hasBlankParams(userid, classroomid, sorttype, sift)) {
			return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		if (StringUtils.isBlank(startNo)) {
			startNo = Constant.DEFAULT_START_NO;
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = Constant.DEFAULT_PAGE_SIZE;
		}
		List<ImproveClassroom> improves = null;
		try {
			if ("1".equals(sorttype)) {
				improves = improveService.selectClassroomImproveList(userid, classroomid, sift,
						Integer.parseInt(startNo), Integer.parseInt(pageSize));

			} else {
				improves = improveService.selectClassroomImproveListByDate(userid, classroomid, sift,
						Integer.parseInt(startNo), Integer.parseInt(pageSize));
			}
		} catch (Exception e) {
			logger.error("select classroom improve list is error:{}", e);
		}
		if (null == improves) {
			return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
		}
		BaseResp<List<ImproveClassroom>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
		baseres.setData(improves);
		return baseres;
	}

	/**
	 * 获取目标中的进步
	 * 
	 * @param userid
	 *            用户id
	 * @param goalid
	 *            目标id
	 * @param startNo
	 *            分页开始条数
	 * @param pageSize
	 *            分页每页显示条数
	 * @return
	 * @author:luye
	 * @date 2017/2/4
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "goal/list", method = RequestMethod.POST)
	public BaseResp selectGoalImproveList(String userid, String goalid, String startNo, String pageSize) {
		if (StringUtils.hasBlankParams(userid, goalid)) {
			return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		if (StringUtils.isBlank(startNo)) {
			startNo = Constant.DEFAULT_START_NO;
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = Constant.DEFAULT_PAGE_SIZE;
		}
		List<ImproveGoal> improves = null;
		try {
			improves = improveService.selectGoalImproveList(userid, goalid, Integer.parseInt(startNo),
					Integer.parseInt(pageSize));
		} catch (Exception e) {
			logger.error("select goal improve list is error:{}", e);
		}
		if (null == improves) {
			return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
		}
		BaseResp<List<ImproveGoal>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
		baseres.setData(improves);
		return baseres;
	}

}
