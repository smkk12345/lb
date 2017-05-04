package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.ImpComplaintsService;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.SysSensitiveService;
import com.longbei.appservice.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
    @Autowired
    private SysSensitiveService sysSensitiveService;
    @Autowired
    private UserServiceImpl userService;


    /**
     * url : http://ip:port/app_service/improve/line/daylist
     *
     * @param userid   用户id
     * @param ptype    十全十美id  不传时查所有
     * @param ctype    0--广场 1--我的 2--好友，关注，熟人 3-好友 4-关注 5-熟人
     * @param lastDate 当天日期
     * @param pageSize 显示条数
     * @return
     * @Description: 获取参数lastdate当天的用户进步列表
     * @author yinxc
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "line/daylist")
    public BaseResp<List<Improve>> daylist(String userid, String ptype, String ctype, String lastDate, String pageSize) {
        logger.info("userid={},ptype={},ctype={},lastDate={},pageSize={}",userid, ptype,ctype,lastDate,pageSize);

        if (StringUtils.hasBlankParams(userid, ctype, lastDate)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        BaseResp<List<Improve>> baseres = new BaseResp<>();
        List<Improve> improves = null;
        try {
            improves = improveService.selectImproveListByUserDate(userid, ptype, ctype,
                    lastDate == null ? null : DateUtils.parseDate(lastDate),
                    Integer.parseInt(pageSize == null ? Constant.DEFAULT_PAGE_SIZE : pageSize));
            baseres.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("line daylist userid = {}, ctype = {}, lastdate = {}", userid, ctype, lastDate, e);
        }
        if (null == improves) {
            return new BaseResp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_43);
        }
        baseres.setData(improves);
        return baseres;
    }

    /**
     * @Title: http://ip:port/app_service/improve/addImpComplaints
     * @Description: 投诉进步
     * @param @param userid 投诉人id
     * @param @param impid 进步id
     * @param @param content 投诉内容
     * @param @param friendid 被投诉人id
     * @param @param contenttype  0：该微进步与龙榜内容不符~~
     * @param @param businessid 类型业务id
     * @param @param businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年2月7日
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "addImpComplaints")
    public BaseResp<ImpComplaints> addImpComplaints(String userid, String friendid, String impid, 
    		String content, String businessid, String contenttype, String businesstype) {
        logger.info("addImpComplaints userid={},friendid={},impid={},content={},businessid={},contenttype={},businesstype={}", userid,friendid,impid, content, businessid,contenttype, businesstype);
        BaseResp<ImpComplaints> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, friendid, impid, contenttype, businesstype)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            ImpComplaints record = new ImpComplaints();
            record.setContent(content);
            record.setContenttype(contenttype);
            record.setCreatetime(new Date());
            record.setBusinesstype(businesstype);
            record.setImpid(Long.parseLong(impid));
            record.setStatus("0");
            record.setUserid(Long.parseLong(userid));
            record.setBusinessid(Long.parseLong(businessid));
            record.setBusinesstype(businesstype);
            record.setComuserid(Long.parseLong(friendid));
            baseResp = impComplaintsService.insertSelective(record, Long.parseLong(friendid));
        } catch (Exception e) {
            logger.error(
                    "addImpComplaints userid = {}, impid = {}, content = {}, contenttype = {}, businesstype = {}",
                    userid, impid, content, contenttype, businesstype, e);
        }
        return baseResp;

    }

    /**
     * 发布进步
     *
     * @param userid       用户id
     * @param brief        说明
     * @param pickey       图片的key
     * @param filekey      文件key 视频文件 音频文件 普通文件
     * @param businesstype 微进步关联的业务类型 0 未关联 1 目标 2 榜 3 圈子 4教室  5：教室批复作业
     * @param businessid   业务id
     * @param ptype        十全十美类型
     * @param ispublic     可见程度 0 私密 1 好友可见 2 全部可见
     * @param itype        类型 0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
     * @param pimpid       : 批复父进步 id businesstype为5时传
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = "insert")
    public BaseResp<Object> insertImprove(String userid, String brief, String pickey, String filekey,
                                          String businesstype, String businessid, String ptype, String ispublic, String itype, String pimpid) {
        logger.info("userid={},brief={},pickey={},filekey={},businesstype={},businessid={},ptype={},ispublic={},itype={},pimpid={}",
                userid,brief,pickey, filekey, businesstype,businessid, ptype,ispublic,itype,pimpid);

        if (StringUtils.hasBlankParams(userid, businesstype, ptype, ispublic, itype)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(brief) && StringUtils.isBlank(pickey) && StringUtils.isBlank(filekey)) {
            return new BaseResp(Constant.STATUS_SYS_40, Constant.RTNINFO_SYS_40);
        }
        BaseResp baseResp = sysSensitiveService.getSensitiveWordSet(brief);
        if (!ResultUtil.isSuccess(baseResp)) {
            return baseResp;
        }
        if (Constant.IMPROVE_CLASSROOM_REPLY_TYPE.equals(businesstype)) {
            //5：教室批复作业
            if (StringUtils.hasBlankParams(pimpid)) {
                return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            }
        }
//		boolean flag = false;
        try {
            baseResp = improveService.insertImprove(userid, brief, pickey, filekey, businesstype, businessid, ptype,
                    ispublic, itype, pimpid);
            if (ResultUtil.isSuccess(baseResp)) {
                logger.debug("insert improve success");
            }
            return baseResp;
        } catch (Exception e) {
            logger.error("insert improve is error:{}", e);
        }
        logger.info("insert improve fail");
        return new BaseResp(Constant.STATUS_SYS_42, Constant.RTNINFO_SYS_42);
    }

    /**
     * 删除进步
     *
     * @param userid       用户id
     * @param improveid    进步id
     * @param businesstype 进步所属业务类型 如 榜，教室等
     * @param businessid   所属业务类型id 如 榜id 教室id
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public BaseResp<Object> removeImprove(String userid, String improveid, String businesstype, String businessid) {
        logger.debug("remove improve userid:{} improveid:{} businesstype:{} businessid:{}", userid, improveid, businesstype, businessid);
        if (StringUtils.hasBlankParams(userid, improveid, businesstype)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if (!"0".equals(businesstype) && StringUtils.isBlank(businessid)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        boolean flag = false;
        try {
            flag = improveService.removeImprove(userid, improveid, businesstype, businessid);
            if (flag) {
                logger.debug("remove improve success");
                return BaseResp.ok(Constant.RTNINFO_SYS_46);
            }
        } catch (Exception e) {
            logger.error("remove improve is error:{}", e);
        }
        logger.info("remove improve fail");
        return new BaseResp(Constant.STATUS_SYS_47, Constant.RTNINFO_SYS_47);
    }

    /**
     * url: http://ip:port/app_service/improve/rank/list method: POST 获取进步列表(榜中)
     *
     * @param userid   用户id
     * @param rankid   榜单id
     * @param sift 筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param sorttype 排序类型（ 0 - 成员动态 1 - 热度 2 - 时间）
     * @param startNum  开始条数
     * @param pageSize 页面显示条数
     * @param lastDate 最后一条时间 在 sorttype=0 时使用
     * @return
     * @author:luye
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "rank/list", method = RequestMethod.POST)
    public BaseResp selectRankImproveList(String userid, String rankid, String sorttype, String sift, String startNum,
                                          String pageSize,String lastDate) {
        logger.info("userid={},rankid={},sorttype={},sift={},startNum={},pageSize={},lastDate={}",
                userid,rankid,sorttype, sift, startNum,pageSize, lastDate);
        if (StringUtils.hasBlankParams(userid, rankid, sorttype, sift)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if ("0".equals(sorttype)){
            if (StringUtils.isBlank(lastDate)){
                lastDate = DateUtils.formatDateTime1(new Date());
//                return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            }
        }
        if (StringUtils.isBlank(startNum)) {
            startNum = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        List<Improve> improves = new ArrayList<>();
        try {
            improves = improveService.selectRankImproveList(userid, rankid, sift, sorttype, Integer.parseInt(startNum),
                    Integer.parseInt(pageSize),lastDate);

        } catch (Exception e) {
            logger.error("select rank improve list is error:{}", e);
        }
        if (null == improves) {
            return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
        }
        BaseResp<List<Improve>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
        baseres.setData(improves);
        return baseres;
    }

    /**
     * 获取进步列表（圈中）
     *
     * @param userid   用户id
     * @param circleid 圈子中id
     * @param sorttype 排序类型 0 - 默认 1 - 动态 2 - 时间）
     * @param startNum  分页开始条数
     * @param pageSize 分页每页显示条数
     * @return
     * @author:luye
     * @date 2017/2/4
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "circle/list", method = RequestMethod.POST)
    public BaseResp selectCircleImproveList(String userid, String circleid, String sorttype, String sift,
                                            String startNum, String pageSize) {
        logger.info("userid={},circleid={},sorttype={},sift={},startNum={},pageSize={}", userid,circleid,sorttype, sift, startNum,pageSize);
        if (StringUtils.hasBlankParams(userid, circleid, sorttype, sift)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(startNum)) {
            startNum = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        List<Improve> improves = null;
        try {
            if ("1".equals(sorttype)) {
                improves = improveService.selectCircleImproveList(userid, circleid, sift, null, Integer.parseInt(startNum),
                        Integer.parseInt(pageSize));

            } else {
                improves = improveService.selectCircleImproveListByDate(userid, circleid, sift, null,
                        Integer.parseInt(startNum), Integer.parseInt(pageSize));
            }
        } catch (Exception e) {
            logger.error("select circle improve list is error:{}", e);
        }
        if (null == improves) {
            return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
        }
        BaseResp<List<Improve>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
        baseres.setData(improves);
        return baseres;
    }

    /**
     * 获取教室中进步列表
     *
     * @param userid      用户id
     * @param classroomid 教室id
     * @param sorttype    排序类型
     * @param startNo     分页开始条数
     * @param pageSize    分页每页显示条数
     * @return
     * @author:luye
     * @date 2017/2/4
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "classroom/list", method = RequestMethod.POST)
    public BaseResp selectClassroomImproveList(String userid, String classroomid, String sorttype, String sift,
                                               String startNo, String pageSize) {
        logger.info("userid={},classroomid={},sorttype={},sift={},startNo={},pageSize={}", userid,classroomid,sorttype, sift, startNo,pageSize);
        if (StringUtils.hasBlankParams(userid, classroomid, sorttype, sift)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(startNo)) {
            startNo = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        List<Improve> improves = null;
        try {
            if ("1".equals(sorttype)) {
                improves = improveService.selectClassroomImproveList(userid, classroomid, sift, null,
                        Integer.parseInt(startNo), Integer.parseInt(pageSize));

            } else {
                improves = improveService.selectClassroomImproveListByDate(userid, classroomid, sift, null,
                        Integer.parseInt(startNo), Integer.parseInt(pageSize));
            }
        } catch (Exception e) {
            logger.error("select classroom improve list is error:{}", e);
        }
        if (null == improves) {
            return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
        }
        BaseResp<List<Improve>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
        baseres.setData(improves);
        return baseres;
    }

    /**
     * 获取目标中的进步
     *
     * @param userid   用户id
     * @param goalid   目标id
     * @param startNum  分页开始条数
     * @param pageSize 分页每页显示条数
     * @return
     * @author:luye
     * @date 2017/2/4
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "goal/list", method = RequestMethod.POST)
    public BaseResp selectGoalImproveList(String userid, String goalid, String startNum, String pageSize) {
        logger.info("userid={},goalid={},startNum={},pageSize={}", userid,goalid,startNum,pageSize);
        if (StringUtils.hasBlankParams(userid, goalid)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(startNum)) {
            startNum = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        List<Improve> improves = null;
        try {
            improves = improveService.selectGoalImproveList(userid, goalid, null, Integer.parseInt(startNum),
                    Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error("select goal improve list is error:{}", e);
        }
        if (null == improves) {
            return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
        }
        BaseResp<List<Improve>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
        baseres.setData(improves);
        return baseres;
    }

    /**
     * url : http://ip:port/app_service/improve/line/list
     *
     * @param userid   用户id
     * @param ptype    十全十美id
     * @param ctype    0--广场 1--我的 2--好友，关注，熟人 3-好友 4-关注 5-熟人
     * @param lastDate 最后一条日期
     * @param pageSize 显示条数
     * @return
     * @author luye
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "line/list", method = RequestMethod.POST)
    public BaseResp selectImproveLineListByUser(String userid, String ptype, String ctype, String lastDate, String pageSize) {
        logger.info("userid={},ptype={},ctype={},lastDate={},pageSize={}", userid,ptype,ctype,lastDate,pageSize);
        Long s = System.currentTimeMillis();
        if (StringUtils.hasBlankParams(userid, ctype)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        List<Improve> improves = null;
        try {
            improves = improveService.selectImproveListByUser(userid, ptype, ctype,
                    lastDate == null ? null : DateUtils.parseDate(lastDate),
                    Integer.parseInt(pageSize == null ? Constant.DEFAULT_PAGE_SIZE : pageSize));
            BaseResp<List<Improve>> baseres = BaseResp.ok(Constant.RTNINFO_SYS_44);
            baseres.setData(improves);

            if(StringUtils.isBlank(lastDate)&&ctype.equals("1")){
                UserInfo userInfo = userService.selectJustInfo(Long.parseLong(userid));
                baseres.getExpandData().put("totalimp",userInfo.getTotalimp());
            }
            Long e = System.currentTimeMillis();
            logger.info("select timeline list need time={}",e-s);
            return baseres;
        } catch (Exception e) {
            logger.error("select improve line list is error:{}", e);
        }
        if (null == improves) {
            return new BaseResp(Constant.STATUS_SYS_43, Constant.RTNINFO_SYS_43);
        }
        return BaseResp.fail();
    }

    /**
     * 获取指定用户所发的全部进步
     * @param userid  用户id
     * @param targetuserid  所要查看的用户id
     * @param lastDate 最后一条的时间
     * @param pageSize 获取数据条数
     * @return
     * @author luye
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "line/targetuserlist", method = RequestMethod.POST)
    public BaseResp<List<Improve>> selectOtherUserImproveList(String userid,String  targetuserid,
                                                              String lastDate, String pageSize){
        logger.info("userid={},targetuserid={},lastDate={},lastDate={},pageSize={}", userid,targetuserid,lastDate,lastDate,pageSize);
        if (StringUtils.hasBlankParams(userid, targetuserid)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        try {
            baseResp = improveService.selectOtherImproveList(userid,targetuserid,
                    lastDate == null ? null : DateUtils.parseDate(lastDate),
                    Integer.parseInt(pageSize == null ? Constant.DEFAULT_PAGE_SIZE : pageSize));
        } catch (Exception e) {
            logger.error("select improve line list is error:{}", e);
        }
        return baseResp;
    }




    /**
     * 点赞
     */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ResponseBody
//	@RequestMapping(value = "like")
//	public BaseResp like(String userid,
//						 String impid,
//						 String businesstype,
//						 String businessid
//						 ) {
//		if (StringUtils.hasBlankParams(userid, impid,businesstype)) {
//			return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
//		}
//		/**
//		 * 点赞每天限制  --- 每天内次只能点一次
//		 * 进步必须是公开的
//		 * 不能给自己点赞
//		 * 取消或者点赞
//		 * 点赞-----进步赞个数  总赞
//		 * 点赞对积分的影响
//		 * 点完赞之后数据返回
//		 */
//		BaseResp<Object> baseResp = new BaseResp<>();
//
//		try{
//			baseResp = improveService.like(userid,impid,businesstype,businessid);
//		}catch (Exception e){
//			logger.error("improveService.like error and msg={}",e);
//		}
//		return baseResp;
//	}

    /**
     * 送花
     */

    /**
     * 送钻
     */

    /**
     * 获取点赞列表
     */

    /**
     * 获取送花列表
     */

    /**
     * 获取送钻列表
     */

    /**
     * url : http://ip:port/app_service/improve/collectImp
     * 收藏进步
     * @param userid       用户uid
     * @param improveid    进步id
     * @param businesstype 进步类型
     * @param businessid   所属id  圈子id  教室id 榜单id
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "collectImp")
    BaseResp<Object> collectImp(String userid, String improveid, String businesstype, String businessid) {
        logger.info("userid={},improveid={},businesstype={},businessid={}", userid,improveid,businesstype,businessid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, improveid, businesstype)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        logger.info("collectImp userid={},impid={}", userid, improveid);
        try {
            baseResp = improveService.collectImp(userid, improveid, businesstype, businessid);
        } catch (Exception e) {
            logger.error("collection error userid={},impid={},msg={}", userid, improveid, e);
        }
        return baseResp;
    }

    /**
     * url : http://ip:port/app_service/improve/removeCollect
     * 取消收藏  improve/removeCollect
     * @param userid
     * @param improveid
     * @param businesstype
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "removeCollect")
    BaseResp<Object> removeCollect(String userid, String improveid, String businesstype) {
        logger.info("userid={},improveid={},businesstype={}", userid,improveid,businesstype);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, improveid, businesstype)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            return improveService.removeCollect(userid, improveid, businesstype);
        } catch (Exception e) {
            logger.error("removeCollect error userid={},impid={} and msg = {}", userid, improveid, e);
        }
        return baseResp;
    }

    /**
     * s获取收藏列表  带分页数据  improve/selectCollect
     *
     * @param userid
     * @param startNum
     * @param pageSize
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "selectCollect")
    public BaseResp<Object> selectCollect(String userid, Integer startNum, Integer pageSize) {
        logger.info("userid={},startNum={},pageSize={}", userid,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }

        try {
            return improveService.selectCollect(userid, startNum, pageSize);
        } catch (Exception e) {
            logger.error("selectCollect error userid={},msg={}", userid, e);
        }
        return baseResp;
    }


    /**
     * 点赞
     *
     * @param userid       用户id
     * @param improveid    进步id
     * @param businesstype 业务类型（榜，圈子等）
     * @param businessid   类型id（榜id，圈子ID等）
     * @param opttype      1 -- 点赞 0 -- 取消赞
     * @return
     * @author luye
     */
    @RequestMapping(value = "addorcancellike")
    @ResponseBody
    public BaseResp<Object> addLikeForImprove(String userid, String improveid, String businesstype, String businessid, String opttype) {
        logger.info("userid={},improveid={},businesstype={},businessid={},opttype={}", userid,improveid,businesstype,businessid,opttype);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, improveid, businesstype, opttype)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if ("1".equals(opttype)) {
            try {
                baseResp = improveService.addlike(userid, improveid, businesstype, businessid);
            } catch (Exception e) {
                logger.error("add like is error:{}", e);
            }
        } else {
            try {
                baseResp = improveService.cancelLike(userid, improveid, businesstype, businessid);
            } catch (Exception e) {
                logger.error("cancel like is error:{}", e);
            }
        }

        return baseResp;
    }

    /**
     * 取消赞
     *
     * @param userid       用户id
     * @param improveid    进步id
     * @param businesstype 业务类型（榜，圈子等）
     * @param businessid   类型id（榜id，圈子ID等）
     * @return
     * @author luye
     */
    @RequestMapping(value = "cancellike")
    @ResponseBody
    public BaseResp<Object> cancelLikeForImprove(String userid, String improveid, String businesstype, String businessid) {
        logger.info("userid={},improveid={},businesstype={},businessid={}", userid,improveid,businesstype,businessid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, improveid, businesstype, businessid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = improveService.cancelLike(userid, improveid, businesstype, businessid);
        } catch (Exception e) {
            logger.error("cancel like is error:{}", e);
        }
        return baseResp;
    }

    /**
     * 送花
     *
     * @param userid       用户id
     * @param improveid    进步id
     * @param flowernum    鲜花数量
     * @param businesstype 业务类型（榜，圈子等）
     * @param businessid   类型id（榜id，圈子ID等）
     * @return
     * @author luye
     */
    @RequestMapping(value = "addflower")
    @ResponseBody
    public BaseResp<Object> addFlowerForImprove(String userid, String friendid, String improveid, String flowernum,
                                                String businesstype, String businessid) {
        logger.info("userid={},friendid={},improveid={},flowernum={},businesstype={},businessid={}", userid,friendid,improveid,flowernum,businesstype,businessid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, improveid, flowernum, businesstype, businessid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = improveService.addFlower(userid, friendid, improveid, Integer.valueOf(flowernum), businesstype, businessid);
        } catch (Exception e) {
            logger.error("add flower flowernum={} userid={} improveid={} is error:{}", flowernum, userid, improveid, e);
        }
        return baseResp;
    }

    /**
     * 送钻石
     *
     * @param userid       用户id
     * @param improveid    进步ID
     * @param diamondnum   钻石数量
     * @param businesstype 业务类型（榜，圈子等）
     * @param businessid   类型id（榜id，圈子ID等）
     * @return
     * @author luye
     */
    @RequestMapping(value = "adddiamond")
    @ResponseBody
    public BaseResp<Object> addDiamondForImprove(String userid, String friendid, String improveid,
                                                 String diamondnum, String businesstype, String businessid) {
        logger.info("userid={},friendid={},improveid={},diamondnum={},businesstype={},businessid={}", userid,friendid,improveid,diamondnum,businesstype,businessid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, improveid, diamondnum, businesstype, businessid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = improveService.addDiamond(userid, friendid, improveid, Integer.valueOf(diamondnum), businesstype, businessid);
        } catch (Exception e) {
            logger.error("add diamond userid={} improve={} diamondnum={} is error:{}", userid, improveid, diamondnum, e);
        }
        return baseResp;
    }

    /**
     * 获取 赞，花，钻 列表
     *
     * @param userid   用户id
     * @param impid    进步id
     * @param opttype  操作类型 0 -- 赞列表 1 -- 送花列表  2--送钻列表
     * @param pageSize 获取条数
     * @param lastDate 最后一条时间 （初次获取可以为null）
     * @return
     * @author luye
     */
    @RequestMapping(value = "lfdlist")
    @ResponseBody
    public BaseResp<List<ImpAllDetail>> getImproveLFDList(String userid, String impid,
                                                          String opttype, String pageSize, String lastDate) {
        logger.info("userid={},impid={},opttype={},pageSize={},lastDate={}", userid,impid,opttype,pageSize,lastDate);
        BaseResp<List<ImpAllDetail>> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, impid, opttype, pageSize)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        Date tempLastDate = null;
        if(StringUtils.isNotEmpty(lastDate)){
            tempLastDate = new Date(Long.parseLong(lastDate));
        }
        try {
            baseResp = improveService.selectImproveLFDList(impid, opttype,
                    Integer.parseInt(pageSize), tempLastDate);

        } catch (Exception e) {
            logger.error("get improve all detail list is error:{}", e);
        }
        return baseResp;
    }

    /**
     * improve/select
     *
     * @param userid
     * @param impid
     * @param businesstype
     * @param businessid
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = "select")
    public BaseResp select(String userid, String impid, String businesstype, String businessid) {
        logger.info("userid={},impid={},businesstype={},businessid={}", userid,impid,businesstype,businessid);
        BaseResp baseResp = new BaseResp();
        if (StringUtils.hasBlankParams(userid, impid)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        logger.info("inprove select userid={},impid={}", userid, impid);
        try {
            baseResp = improveService.select(userid, impid, businesstype, businessid);
            if(ResultUtil.isSuccess(baseResp)){
                baseResp.getExpandData().put("shareurl", AppserviceConfig.h5_share_improve_detail);
            }
            return baseResp;
        } catch (Exception e) {
            logger.error("get improve detail  is error userid={},impid={} ", userid, impid, e);
        }
        return new BaseResp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
    }

    /**
     * 获取进步推荐列表
     * @param userid 用户id
     * @param startNum 开始条数
     * @param pageSize 每页显示条数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "recommendlist")
    public BaseResp<List<Improve>> selectRecommendImproveList(String userid,String startNum,String pageSize) {
        logger.info("userid={},startNum={},pageSize={}", userid,startNum,pageSize);
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid)) {
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        if (StringUtils.isBlank(startNum)) {
            startNum = "1";
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = improveService.selectRecommendImproveList(userid, Integer.parseInt(startNum),
                    Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error("select recommend improve list for app is error:", e);
        }
        return baseResp;
    }

    /**
     * 查询单个用户在榜单中发布的进步列表
     * @param curuserid
     * @param userid
     * @param rankid
     * @param startNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectListInRank")
    public BaseResp selectListInRank(String curuserid,String userid, String rankid, Integer startNum,Integer pageSize) {
        logger.info("curuserid={},userid={},rankid={},startNum={},pageSize={}", curuserid,userid,rankid,startNum,pageSize);
        if (StringUtils.hasBlankParams(curuserid,userid, rankid)) {
            return new BaseResp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if(null == startNum){
            startNum = 0;
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        logger.info("inprove select userid={},impid={}", userid);
        try {
            return improveService.selectListInRank(curuserid,userid,rankid,
                    Constant.IMPROVE_RANK_TYPE,startNum,pageSize);
        } catch (Exception e) {
            logger.error("get improve detail  is error userid={},impid={} ", userid, e);
        }
        return null;
    }


}
