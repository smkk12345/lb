package com.longbei.appservice.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.NumberUtil;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.RankService;
import com.longbei.appservice.service.RankSortService;
import com.longbei.appservice.service.UserIdcardService;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.collection.immutable.Stream;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 榜单操作接口实现类
 *
 * @author luye
 * @create 2017-01-20 下午3:29
 **/
@Service("rankService")
public class RankServiceImpl extends BaseServiceImpl implements RankService{

    private static Logger logger = LoggerFactory.getLogger(RankServiceImpl.class);

    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private RankImageMapper rankImageMapper;
    @Autowired
    private RankCheckDetailMapper rankCheckDetailMapper;
    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private RankAwardMapper rankAwardMapper;
    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private RankAwardReleaseMapper rankAwardReleaseMapper;
    @Autowired
    private RankMembersMapper rankMembersMapper;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private SpringJedisDao springJedisDao;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserIdcardService userIdcardService;
    @Autowired
    private ImproveRankMapper improveRankMapper;
    @Autowired
    private RankSortService rankSortService;

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public boolean insertRank(RankImage rankImage) {

        rankImage.setRankid(idGenerateService.getUniqueIdAsLong());
        rankImage.setCreatetime(new Date());

        int res = 0;
        try {
            res = rankImageMapper.insertSelective(rankImage);
            insertRankAward(String.valueOf(rankImage.getRankid()),rankImage.getRankAwards());
        } catch (Exception e) {
            logger.error("insert rank:{} is error:{}", JSONObject.fromObject(rankImage),e);
        }
        return res != 0;
    }

    @Override
    public boolean updateRankImageSymbol(RankImage rankImage) {
        int res = 0;
        try {
            res = rankImageMapper.updateSymbolByRankId(rankImage);
            if (res > 0) {
                if (Constant.RANK_ISAUTO_TIME.equals(rankImage.getIsauto())){
                    //定时任务
                }
            }
        } catch (Exception e) {
            logger.error("update rank image symbol is error:",e);
        }
        return res>0;
    }

    @Override
    public boolean updateRankSymbol(Rank rank) {
        int res = 0;
        try {
            res = rankMapper.updateSymbolByRankId(rank);
        } catch (Exception e) {
            logger.error("update rank symbol is error:",e);
        }
        return res>0;
    }

    private boolean insertRankAward(String rankid, List<RankAward> rankAwards){
        if (null != rankAwards){
            for (RankAward rankAward:rankAwards){
                rankAward.setRankid(rankid);
                rankAward.setCreatetime(new Date());
            }
            try {
                int res = rankAwardMapper.insertBatch(rankAwards);
                return true;
            } catch (Exception e) {
                logger.error("insert rank award rankid={} is error:",rankid,e);
            }
        }
        return false;
    }

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午6:12
     *  @update 2017/1/23 下午6:12
     */
    @Override
    public boolean updateRankImage(RankImage rankImage) {
        int res = 0;
        try {
            res = rankImageMapper.updateByPrimaryKeySelective(rankImage);
            rankAwardMapper.deleteByRankid(String.valueOf(rankImage.getRankid()));
            insertRankAward(String.valueOf(rankImage.getRankid()),rankImage.getRankAwards());
        } catch (Exception e) {
            logger.error("update rank:{} is error:{}", JSONObject.fromObject(rankImage),e);
        }
        return res != 0;
    }

    @Override
    public BaseResp<RankImage> selectRankImage(String rankimageid) {
        try {
            RankImage rankImage = rankImageMapper.selectByRankImageId(rankimageid);
            rankImage.setRankAwards(selectRankAwardByRankid(rankimageid));
            BaseResp<RankImage> baseResp = BaseResp.ok();
            baseResp.setData(rankImage);
            return baseResp;
        } catch (Exception e) {
            logger.error("select rank image by rankimageid={} is error:{}",rankimageid,e);
        }
        return BaseResp.fail();
    }

    private List<RankAward> selectRankAwardByRankid(String rankiamgeid){
        List<RankAward> rankAwards = rankAwardMapper.selectListByRankid(rankiamgeid);
        for (RankAward rankAward : rankAwards){
            Award award = awardMapper.selectByPrimaryKey(Integer.parseInt(rankAward.getAwardid()));
            rankAward.setAward(award);
        }
        return rankAwards;
    }

    @Override
    public BaseResp publishRankImage(String  rankImageid) {
        BaseResp<RankImage> baseResp = selectRankImage(rankImageid);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }
        RankImage rankim = baseResp.getData();
        if (!Constant.RANKIMAGE_STATUS_4.equals(rankim.getCheckstatus())){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_60, Constant.RTNINFO_SYS_60);
        }
        Rank rank = new Rank();
        try {
            BeanUtils.copyProperties(rank,rankim);
        } catch (Exception e) {
            logger.error("copy rankimage to rank is error:{}",e);
        }
        try {
            Rank rank1 = rankMapper.selectRankByRankid(rankim.getRankid());
            int res = 0;
            boolean flag = updateRankAwardRelease(rankImageid);
            if (flag){
                if (null != rank1){
                    res = rankMapper.updateByPrimaryKeySelective(rank);
                } else {
                    res = rankMapper.insertSelective(rank);
                }
                if (res > 0){
                    RankImage rm = new RankImage();
                    rm.setRankid(Long.valueOf(rankImageid));
                    rm.setIsup(Constant.RANK_ISUP_YES);
                    rankImageMapper.updateSymbolByRankId(rm);
                    return BaseResp.ok();
                }
            }

        } catch (Exception e) {
            logger.error("publish rank rankid={} is error:",rankImageid,e);
        }
        return BaseResp.fail();
    }

    private boolean deleteRankAwardRelease(String rankid){
        boolean flag = true;
        try {
            rankAwardReleaseMapper.deleteByRankid(rankid);
        } catch (Exception e) {
            flag = false;
            logger.error("delete rankaward release is error:",e);
        }
        return flag;
    }

    private boolean updateRankAwardRelease(String rankimageid){
        List<RankAward> awards = null;
        try {
            awards = rankAwardMapper.selectListByRankid(rankimageid);
        } catch (Exception e) {
            logger.error("select rankaward by rankid id={} is error:",rankimageid,e);
            return false;
        }
        boolean flag = deleteRankAwardRelease(rankimageid);
        if (flag){
            List<RankAwardRelease> rankAwardReleases = new ArrayList<>();
            for (RankAward rankAward : awards){
                RankAwardRelease rankAwardRelease = new RankAwardRelease();
                try {
                    BeanUtils.copyProperties(rankAwardRelease,rankAward);
                } catch (Exception e) {
                    logger.warn("copy rankaward to rankawardrelease is error:",e);
                    return false;
                }
                rankAwardReleases.add(rankAwardRelease);
            }
            try {
                rankAwardReleaseMapper.insertBatch(rankAwardReleases);
                return true;
            } catch (Exception e) {
                logger.error("insert batch rankawardrelease is error:",e);
            }
        }
        return false;
    }

    @Override
    public Page<RankImage> selectRankImageList(RankImage rankImage,int pageno, int pagesize) {
        Page<RankImage> page = new Page<>(pageno,pagesize);
        try {
            int totalcount = rankImageMapper.selectListCount(rankImage);
            pageno = Page.setPageNo(pageno,totalcount,pagesize);
            List<RankImage> rankImages = rankImageMapper.selectListWithPage(rankImage,(pageno-1)*pagesize,pagesize);
            page.setTotalCount(totalcount);
            page.setList(rankImages);
        } catch (Exception e) {
            logger.error("select rank image list is error:",e);
        }
        return page;
    }

    @Override
    public Page<Rank> selectRankList(Rank rank, int pageno, int pagesize) {
        Page<Rank> page = new Page<>(pageno,pagesize);
        try {
            int totalcount = rankMapper.selectListCount(rank);
            pageno = Page.setPageNo(pageno,totalcount,pagesize);
            List<Rank> ranks = rankMapper.selectListWithPage(rank,(pageno-1)*pagesize,pagesize);
            page.setTotalCount(totalcount);
            page.setList(ranks);
        } catch (Exception e) {
            logger.error("select rank list for adminservice is error:",e);
        }
        return page;
    }

    @Override
    public boolean deleteRankImage(String rankimageid) {
        int res = 0;
        try {
            res = rankImageMapper.deleteByRankImageId(rankimageid);
        } catch (Exception e) {
            logger.error("delete rank image by rankimageid={} is error:{}",rankimageid,e);
        }
        return res > 0;
    }

    @Override
    public boolean updateSponsornumAndSponsormoney(long rankid ) {
        int res = 0;
        try {
            res = rankMapper.updateSponsornumAndSponsormoney(rankid);
        } catch (Exception e) {
            logger.error("updateSponsornumAndSponsormoney error:{}", e);
        }
        return res > 0;
    }

    @Override
    public BaseResp checkRankImage(RankCheckDetail rankCheckDetail) {
        BaseResp baseResp = new BaseResp();
        RankImage rankImage = new RankImage();
        rankImage.setRankid(rankCheckDetail.getRankid());
        rankImage.setCheckstatus(rankCheckDetail.getCheckstatus());
        boolean flag = updateRankImageSymbol(rankImage);
        if (flag) {
            int res = 0;
            try {
                res = rankCheckDetailMapper.insertSelective(rankCheckDetail);
            } catch (Exception e) {
                logger.error("insert rank check detail is error:{}",e);
            }
            if (res > 0){
                publishRankImageByCheckStatus(rankCheckDetail);
                return BaseResp.ok();
            }
        }
        return baseResp;
    }

    /**
     * 判断是否发布榜单
     */
    private void publishRankImageByCheckStatus(RankCheckDetail rankCheckDetail){
        //审核通过
        if (Constant.RANKIMAGE_STATUS_4.equals(rankCheckDetail.getCheckstatus())){
            BaseResp<RankImage> baseResp = selectRankImage(String.valueOf(rankCheckDetail.getRankid()));
            if(ResultUtil.isSuccess(baseResp)){
                RankImage rankImage = baseResp.getData();
                if (Constant.RANK_ISAUTO_YES.equals(rankImage.getIsauto())){
                    publishRankImage(String.valueOf(rankCheckDetail.getRankid()));
                }
            }
        }
        //审核不通过 不可以修改 直接删除
        if (Constant.RANKIMAGE_STATUS_3.equals(rankCheckDetail.getCheckstatus())){
            deleteRankImage(String.valueOf(rankCheckDetail.getRankid()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public BaseResp<Object> selectRankByRankid(long rankid) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            Rank rank = rankMapper.selectRankByRankid(rankid);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            baseResp.setData(rank);
        } catch (Exception e) {
            logger.error("selectRankByRankid error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public Rank selectByRankid(long rankid) {
        try {
            Rank rank = rankMapper.selectRankByRankid(rankid);
            return rank;
        } catch (Exception e) {
            logger.error("selectRankByRankid error and msg={}",e);
        }
        return null;
    }

    /**
     * 用户加入榜单
     * @param userId 用户id
     * @param rankId 榜单id
     * @param codeword 口令
     * @return
     */
    @Override
    public BaseResp<Object> insertRankMember(Long userId, Long rankId, String codeword) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            Rank rank = this.rankMapper.selectRankByRankid(rankId);
            if (rank == null) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            }
            if ("1".equals(rank.getIsfinish()) || "1".equals(rank.getIspublic()) || "1".equals(rank.getIsdel())) {
                return baseResp.fail("抱歉,由于龙榜已结束或未设置为开放等原因,您暂时无法进行参榜!");
            }
            if (!DateUtils.compare(rank.getEndtime(), new Date())) {
                return baseResp.fail("非常抱歉,该榜单已结束!");
            }
            //查看口令是否正确
            if(StringUtils.isNotEmpty(rank.getCodeword()) && (StringUtils.isEmpty(codeword) || !codeword.equals(rank.getCodeword()))){
                return baseResp.fail("口令不正确,请重新输入口令!");
            }
            if("1".equals(rank.getIsrealname())){
                UserIdcard userIdCard = this.userIdcardService.selectByUserid(userId+"");
                if(!"2".equals(userIdCard.getValidateidcard())){
                    return baseResp.fail("加入该榜单,需要用户实名认证,您还未实名认证通过");
                }
            }

            if("1".equals(rank.getIslonglevel())){
                UserInfo userInfo = this.userInfoMapper.selectByUserid(userId);
                if(rank.getLonglevel() != null && (userInfo.getGrade() < Integer.parseInt(rank.getLonglevel()))){
                    return baseResp.fail("由于您的等级未满足参榜的等级要求,请先提高自己的等级吧!");
                }
            }
            if((rank.getRankinvolved() + 1) > rank.getRanklimite()){
                return baseResp.fail("抱歉,参榜人数已达榜最大限制人数!");
            }

            //校验用户是否已经在榜单中
            RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(rankId, userId);
            if(rankMembers != null && rankMembers.getStatus() == 0 ){
                return baseResp.fail("您已申请加入了龙榜,正在等待榜主审核!");
            }else if(rankMembers != null && rankMembers.getStatus() == 1){
                return baseResp.fail("您已在该龙榜中,无需再申请重新加入!");
            }else if(rankMembers != null && rankMembers.getStatus() == 3){
                return baseResp.fail("由于您被群主踢出了该龙榜,因此无法再次申请加入!");
            }else if(rankMembers != null && rankMembers.getStatus() == 2){
                //修改数据
                //1.修改用户的状态为入榜
                int status = 0;
                if(rank.getNeedConfirm() != null && !rank.getNeedConfirm()){
                    status = 1;
                }
                boolean initRankMember = true;
                Map<String,Object> map = new HashMap<>();
                map.put("userId",userId);
                map.put("rankId",rankId);
                map.put("initRankMember",initRankMember);
                int updateRankMemberRow = this.rankMembersMapper.updateRank(map);

                //2.如果直接入榜 不需要审核的话,则修改入榜人数
                if(status == 1 && updateRankMemberRow > 0){
                    boolean updateRankMemberCount = updateRankMemberCount(rankId,1);
                }

                //初始化redis人数
                boolean redisInitFlag = initRedisRankSort(rank,userId);

                //TODO 发送消息给榜主

                return baseResp.ok("入榜成功!");
            }

            RankMembers rankMember = new RankMembers();
            rankMember.setCreatetime(new Date());
            rankMember.setFlowers(0);
            rankMember.setIcount(0);
            rankMember.setUserid(userId);
            rankMember.setRankid(rankId);
            rankMember.setUpdatetime(new Date());
            rankMember.setLikes(0);
            rankMember.setIsfashionman("0");
            if(rank.getNeedConfirm() != null && rank.getNeedConfirm()){
                rankMember.setStatus(0);
            }else{
                rankMember.setStatus(1);
            }
            int row = rankMembersMapper.insertSelective(rankMember);
            if(row > 0 && rankMember.getStatus() == 1){
                //往reids中放入初始化的排名值
                boolean initRedisFlag = initRedisRankSort(rank,userId);
                boolean updateRankFlag = updateRankMemberCount(rankId,1);

                //TODO 发送消息给榜主

                return baseResp.ok();
            }
        } catch (Exception e) {
            logger.error("insert rankMemeber error rankId:{} userId:{}",rankId,userId);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 退榜
     * @param userId
     * @param rankId
     * @return
     */
    @Override
    public BaseResp<Object> removeRankMember(Long userId, Long rankId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Rank rank = rankMapper.selectRankByRankid(rankId);
            if(rank == null){
                return baseResp.fail("参数错误");
            }
            if(userId.equals(rank.getCreateuserid())){
                return baseResp.fail("榜主不可退榜");
            }

            //1.更改rankMember的状态
            Map<String,Object> updateMap = new HashMap<String,Object>();
            updateMap.put("rankId",rankId);
            updateMap.put("userId",userId);
            updateMap.put("status","2");
            int updateRow = this.rankMembersMapper.updateRank(updateMap);
            if(updateRow < 1){
                return baseResp.fail("退榜失败");
            }
            //2.更改用户在该榜单中发布的进步的状态
            int removeRow = improveRankMapper.updateImproveRanStatus(userId+"",rankId+"",null,"1");

            //3.更改参榜人数
            boolean updateRankFlag = updateRankMemberCount(rankId,-1);
            //4.删除reids中榜单的该用户排名
            boolean redisRemoveFlag = springJedisDao.zRem(Constant.REDIS_RANK_SORT+rankId,userId+"");
            return baseResp.ok("退榜成功");
        }catch(Exception e){
            logger.error("remove RankMember error userId:{} rankId:{}",userId,rankId);
            printException(e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> removeRankMember(RankMembers rankMembers) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            Rank rank = rankMapper.selectRankByRankid(rankMembers.getRankid());
            if(rank == null){
                return baseResp.fail("参数错误");
            }
            if(rankMembers.getUserid().equals(rank.getCreateuserid())){
                return baseResp.fail("榜主不可退榜");
            }
            int updateRow = this.rankMembersMapper.updateRankMemberState(rankMembers);
            if(updateRow < 1){
                return baseResp.fail("退榜失败");
            }
            //2.更改用户在该榜单中发布的进步的状态
            int removeRow = improveRankMapper.updateImproveRanStatus(rankMembers.getUserid()+"",
                    rankMembers.getRankid()+"",null,"1");
            //3.更改参榜人数
            boolean updateRankFlag = updateRankMemberCount(rankMembers.getRankid(),-1);
            //4.删除reids中榜单的该用户排名
            boolean redisRemoveFlag = springJedisDao.zRem(Constant.REDIS_RANK_SORT+rankMembers.getRankid(),
                    rankMembers.getUserid()+"");
            return baseResp.ok("退榜成功");

        } catch (Exception e) {
            logger.error("remove RankMember error userId:{} rankId:{}",rankMembers.getUserid(),rankMembers.getRankid());
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> setIsfishionman(RankMembers rankMembers) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            int res = rankMembersMapper.updateRankMemberState(rankMembers);
            if (res > 0){
                return BaseResp.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> updateRankMemberCheckStatus(RankMembers rankMembers) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            int res = rankMembersMapper.updateRankMemberState(rankMembers);
            if (res > 0){
                return BaseResp.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResp;
    }

    /**
     * 更改用户的参榜申请
     * @param userIds 用户id 数组
     * @param rankId 榜单id
     * @param status 要处理的结果
     * @return
     */
    @Override
    public BaseResp<Object> auditRankMember(Long[] userIds, Long rankId, Integer status) {
        BaseResp<Object> baseResp =new BaseResp<>();
        try{
            Rank rank = this.rankMapper.selectRankByRankid(rankId);
            Map<String,Object> updateMap = new HashMap<String,Object>();
            updateMap.put("rankId",rankId);
            updateMap.put("status",status);
            updateMap.put("initRankMember",false);

            boolean flag = true;
            //牵扯到是否需要往redis中初始化用户排名,以及批量处理申请时,可能会超过人数限制,所以用了循环
            for(Long userId:userIds){
                RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rankId,userId);
                if(rankMembers == null || status.equals(rankMembers.getStatus())){
                    continue;
                }
                if(status == 1 && (rank.getRankinvolved()+1) > rank.getRanklimite()){
                    baseResp.fail("榜单人数已达上限!");
                    flag = false;
                    break;
                }
                //1.修改rankMember中的状态
                updateMap.put("userId",userId);
                int updateRow = this.rankMembersMapper.updateRank(updateMap);
                if(updateRow < 1){
                    continue;
                }
                //2.参榜人数加1
                boolean updateRankMemberCountFlag = updateRankMemberCount(rankId,1);
                if(updateRankMemberCountFlag){
                    rank.setRankinvolved(rank.getRankinvolved()+1);
                }
                //3.初始化redis的用户排名
                boolean redisFlag = initRedisRankSort(rank,userId);
            }
            if(flag){
                return baseResp.ok();
            }
            return baseResp;
        }catch(Exception e){
            logger.error("audit rankMemeber error userIds:{} rankId:{} status:{}",userIds,rankId,status);
            printException(e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> submitRankMemberCheckResultPreview(String rankid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        if (!isSubRankMemberCheckResult(rankid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_54,Constant.RTNINFO_SYS_54);
        }
        try {
            //发布奖品
            publishRankAward(rankid);
            return BaseResp.ok();
        } catch (Exception e) {
            logger.error("submint rank member checkresult rankid={} is error:",rankid,e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Object> submitRankMemberCheckResult(Rank rank) {
        BaseResp<Object>  baseResp = submitRankMemberCheckResultPreview(String.valueOf(rank.getRankid()));
        if (ResultUtil.isSuccess(baseResp)){
            int res = rankMapper.updateSymbolByRankId(rank);
            if (res > 0){
                return baseResp;
            }
        }
        return BaseResp.fail();
    }

    @Override
    public BaseResp<Page<RankMembers>> rankMemberCheckResultPreview(RankMembers rankMembers, Integer pageNo, Integer pageSize) {
        Rank rank = rankMapper.selectRankByRankid(rankMembers.getRankid());
        BaseResp baseResp = new BaseResp();
        if (null != rank && "3".equals(rank.getIsfinish())){
            baseResp = BaseResp.ok();
        }else{
            baseResp = submitRankMemberCheckResultPreview(String.valueOf(rankMembers.getRankid()));
        }

        if (ResultUtil.isSuccess(baseResp)) {
            rankMembers.setCheckstatus("-1");
            BaseResp<Page<RankMembers>> pageBaseResp = selectRankMemberList(rankMembers, pageNo, pageSize);
            return pageBaseResp;
        }
        return baseResp;
    }
    /**
     * 查询用户在榜单中的排名
     * @param rankId 榜单id
     * @param userId 用户id
     * @return
     */
    @Override
    public BaseResp<Object> ownRankSort(Long rankId, Long userId) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rankId,userId);
            if(rankMembers == null || rankMembers.getStatus() != 1){
                return baseResp.fail("您当前还未加入该榜单,或已退出该榜单");
            }
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("likes",rankMembers.getLikes());
            resultMap.put("flowers",rankMembers.getFlowers());

            long sort = this.springJedisDao.zRevRank(Constant.REDIS_RANK_SORT+rankId,userId+"");
            resultMap.put("sort",sort);

            resultMap.put("likes",rankMembers.getLikes());
            resultMap.put("flowers",rankMembers.getFlowers());
            AppUserMongoEntity appUserMongoEntity = this.userMongoDao.findById(userId+"");
            resultMap.put("nickName",appUserMongoEntity.getNickname());
            resultMap.put("avatar",appUserMongoEntity.getAvatar());

            baseResp.setData(resultMap);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("own rank sort error rankId:{} userId:{}",rankId,userId);
            printException(e);
        }

        return baseResp;
    }

    /**
     * 榜单的成员排名列表
     * @param rankId 榜单id
     * @param sortType 排序的类型
     * @param startNum 开始下标
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public BaseResp<Object> rankMemberSort(Long rankId, Constant.SortType sortType, Integer startNum, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            List<RankMembers> userList = new ArrayList<RankMembers>();
            if(Constant.SortType.comprehensive.equals(sortType)){
                Integer endNum = startNum + pageSize - 1;
                Set<String> userIdList  = this.springJedisDao.zRevrange(Constant.REDIS_RANK_SORT+rankId,startNum,endNum);
                if(userIdList != null && userIdList.size() > 0){
                    int i = 0;
                    for(String userId:userIdList){
                        RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rankId,Long.parseLong(userId));
                        rankMembers.setSortnum(startNum+i+1);
                        rankMembers.setAppUserMongoEntity(userMongoDao.findById(userId+""));
                        userList.add(rankMembers);
                        i++;
                    }
                }
            }else{
                userList = this.rankMembersMapper.selectUserSort(rankId,sortType.toString(),startNum,pageSize);
                if(userList != null && userList.size() > 0){
                    int i = 0;
                    for (RankMembers rankMember:userList){
                        rankMember.setSortnum(startNum + i +1);
                        rankMember.setAppUserMongoEntity(userMongoDao.findById(rankMember.getUserid()+""));
                        i++;
                    }
                }
            }

            baseResp.setData(userList);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("rank member sort list error rankId:{} sortType:{} startNum:{} pageSize:{}",rankId,sortType,startNum,pageSize);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 获取榜中的达人
     * @param rankId 榜单id
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Object> selectFashionMan(Long rankId, Integer startNum, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> parameterMap = new HashMap<String,Object>();
            parameterMap.put("rankId",rankId);
            parameterMap.put("isFashionMan","1");
            parameterMap.put("startNum",startNum);
            parameterMap.put("pageSize",pageSize);
            List<RankMembers> rankMembersList = this.rankMembersMapper.selectRankMembers(parameterMap);
            if(rankMembersList != null && rankMembersList.size() > 0){
                for (RankMembers rankMembers : rankMembersList) {
                    rankMembers.setAppUserMongoEntity(this.userMongoDao.findById(rankMembers.getUserid()+""));
                }
            }
            baseResp.setData(rankMembersList);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select rank fashionMain error rankId:{} startNum:{} pageSize:{}",rankId,startNum,pageSize);
            printException(e);
        }
        return baseResp;
    }

    /**\
     * 用户领奖
     * @param userId 用户id
     * @param rankId 榜单id
     * @return
     */
    @Override
    public BaseResp<Object> acceptAward(Long userId, Long rankId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            RankMembers rankMember = this.rankMembersMapper.selectByRankIdAndUserId(rankId,userId);
            if(rankMember != null && !"1".equals(rankMember.getIswinning())){//判断是否获奖
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_66,Constant.RTNINFO_SYS_66);
            }else if(rankMember != null && !"0".equals(rankMember.getAcceptaward())){//已经领过奖了
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_65,Constant.RTNINFO_SYS_65);
            }
            //查看该用户获得的是什么奖
            

        }catch(Exception e){
            logger.error("user accept award error userId:{} rankId:{}",userId,rankId);
            printException(e);
        }

        return baseResp;
    }

    private List<RankAwardRelease> selectRankAwardByRankidRelease(String rankid){
        List<RankAwardRelease> rankAwards = rankAwardReleaseMapper.selectListByRankid(rankid);
        for (RankAwardRelease rankAward : rankAwards){
            Award award = awardMapper.selectByPrimaryKey(Integer.parseInt(rankAward.getAwardid()));
            rankAward.setAward(award);
        }
        return rankAwards;
    }


    /**
     * 更改榜中的参榜人数
     * @param rankId 榜id
     * @param count 新增人数
     * @return
     */
    private boolean updateRankMemberCount(Long rankId, int count) {
        int updateRow = this.rankMapper.updateRankMemberCount(rankId,count);
        return updateRow > 0? true:false;
    }

    /**
     * 初始化新入榜的用户排名
     * @param rank 榜单信息
     * @param userId
     * @return
     */
    private boolean initRedisRankSort(Rank rank, Long userId) {
        long nowDate = new Date().getTime();
        double a = nowDate - rank.getCreatetime().getTime();
        double b = rank.getEndtime().getTime() - rank.getCreatetime().getTime();
        double ratio = NumberUtil.round(a/b,5);
        ratio = 1 - ratio;
        return springJedisDao.zAdd(Constant.REDIS_RANK_SORT+rank.getRankid(),userId+"",ratio);
    }


    @Override
    public BaseResp<Rank> selectRankDetailByRankid(String rankid) {
        BaseResp<Rank> baseResp = new BaseResp();
        try {

            Rank rank = rankMapper.selectRankByRankid(Long.parseLong(rankid));
            rankSortService.checkRankEnd(rank);
            if (null != rank){
                rank.setRankAwards(selectRankAwardByRankidRelease(String.valueOf(rankid)));
                rank.setAppUserMongoEntity(userMongoDao.findById(rank.getCreateuserid()+""));
            }else{
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            baseResp.setData(rank);
        } catch (Exception e) {
            logger.error("selectRankByRankid error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Page<RankMembers>> selectRankMemberList(RankMembers rankMembers, Integer pageNo, Integer pageSize) {
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers || null == rankMembers.getRankid()){
            return baseResp;
        }
        Page<RankMembers> page = new Page<>(pageNo,pageSize);
        try {
            AppUserMongoEntity user = rankMembers.getAppUserMongoEntity();
            List<AppUserMongoEntity> users = null;
            if (null != user) {
                users = userMongoDao.getAppUsers(user);
            }
            rankMembers.setAppUserMongoEntities(users);
            int totalcount = rankMembersMapper.selectCount(rankMembers);
            List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,pageSize*(pageNo-1),pageSize);
            for (RankMembers rankMembers1 : rankMemberses){
                rankMembers1.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(rankMembers1.getUserid())));
                if (null != rankMembers1.getRankAward() && null != rankMembers1.getRankAward().getAwardid()){
                    rankMembers1.getRankAward().setAward(awardMapper.selectByPrimaryKey(Integer.parseInt(rankMembers1.getRankAward().getAwardid())));
                }
            }
            page.setTotalCount(totalcount);
            page.setList(rankMemberses);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select rankmembers list rankid={} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Page<RankMembers>> selectRankMemberWaitCheckList(RankMembers rankMembers, Integer pageNo, Integer pageSize) {
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers || null == rankMembers.getRankid()){
            return baseResp;
        }
        Page<RankMembers> page = new Page<>(pageNo,pageSize);
        try {
            int totalcount = selectRankMemberWaitCount(rankMembers);
            AppUserMongoEntity user = rankMembers.getAppUserMongoEntity();
            List<AppUserMongoEntity> users = null;
            if (null != user) {
                users = userMongoDao.getAppUsers(user);
            }
            rankMembers.setAppUserMongoEntities(users);
            List<RankMembers> rankMemberses = rankMembersMapper.selectWaitCheckList
                    (rankMembers,pageSize*(pageNo-1),pageSize,totalcount);
            for (RankMembers rankMembers1 : rankMemberses){
                rankMembers1.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(rankMembers1.getUserid())));

            }
            page.setTotalCount(totalcount);
            page.setList(rankMemberses);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select wait check rankmembers list rankid={} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }

    /**
     * 查询榜单待审核成员数
     * @param rankMembers
     * @return
     */
    private int selectRankMemberWaitCount(RankMembers rankMembers){
        Rank rank = rankMapper.selectRankByRankid(rankMembers.getRankid());
        //榜单参与人数
        int rankmnum = rank.getRankinvolved();
        //榜单设置奖品数
        int awardcount = getRankAwardCount(String.valueOf(rankMembers.getRankid()));
        //审核通过数
        rankMembers.setCheckstatus("3");
        int okcount = rankMembersMapper.selectCount(rankMembers);
        //未审核数
        rankMembers.setCheckstatus("0");
        int waitcount = rankMembersMapper.selectCount(rankMembers);
        if (awardcount > rankmnum){
            awardcount = rankmnum;
        }
        if (waitcount <= awardcount - okcount){
            return waitcount;
        } else {
            return (awardcount - okcount)<0?0:(awardcount - okcount);
        }
    }

    private boolean isSubRankMemberCheckResult(String rankid){
        RankMembers rankMembers = new RankMembers();
        rankMembers.setRankid(Long.parseLong(rankid));

        //榜单设置奖品数
        int awardcount = getRankAwardCount(String.valueOf(rankMembers.getRankid()));
        //审核通过数
        rankMembers.setCheckstatus("3");
        int okcount = rankMembersMapper.selectCount(rankMembers);
        //未审核数
        rankMembers.setCheckstatus("0");
        int waitcount = rankMembersMapper.selectCount(rankMembers);

        //通过数等于奖品数
        if (okcount == awardcount){
            return true;
        } else if (okcount < awardcount){
            if (waitcount==0){
                return true;
            }
        } else {
            return true;
        }
        return false;
    }


    /**
     * 发布奖品
     * @param rankid
     */
    private void publishRankAward(String rankid){
        RankMembers rankMembers = new RankMembers();
        rankMembers.setRankid(Long.parseLong(rankid));
        //获取榜单全部成员列表
        List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,null,null);
        //获得榜单奖品
        List<RankAward> rankAwards = rankAwardMapper.selectListByRankid(rankid);
        int awardcount = 0;
        int tempcount = 0;
        for(int i=0;i<rankAwards.size();i++){
            RankAward rankAward = rankAwards.get(i);
            for (int j = tempcount ; j < rankMemberses.size();j++){
                if (awardcount > rankAward.getAwardrate()){
                    break;
                }
                RankMembers rkmember = rankMemberses.get(j);
                if ("3".equals(rkmember.getCheckstatus())){
                    awardcount++;
                    tempcount++;
                    rankMembers.setUserid(rkmember.getUserid());
                    rankMembers.setIswinning("1");
                    RankAward rankAward1 = new RankAward();
                    rankAward1.setAwardlevel(i+1);
                    rankAward1.setAwardid(rankAward.getAwardid());
                    rankMembers.setRankAward(rankAward1);
                    rankMembersMapper.updateRankMemberState(rankMembers);
                }
                if ("1".equals(rkmember.getCheckstatus())
                        || "2".equals(rkmember.getCheckstatus())){
                    tempcount++;
                    rankMembers.setUserid(rkmember.getUserid());
                    rankMembers.setIswinning("2");
                    rankMembersMapper.updateRankMemberState(rankMembers);
                }
            }
            awardcount = 0;
        }
    }


    private int getRankAwardCount(String rankid){
        int awardcont = 0;
        try {
            List<RankAward> rankAwards = rankAwardMapper.selectListByRankid(rankid);
            if (null != rankAwards && rankAwards.size() != 0){
                for (RankAward rankAward : rankAwards){
                    awardcont += rankAward.getAwardrate();
                }
            }
        } catch (Exception e) {
            logger.error("get rankaward rankid={} count is error:",rankid,e);
        }
        return awardcont;
    }



    @Override
    public BaseResp<RankMembers> selectRankMemberInfo(String rankid, String userid) {
        BaseResp<RankMembers> baseResp = new BaseResp<>();
        RankMembers rankMembers = new RankMembers();
        try {
            rankMembers.setRankid(Long.parseLong(rankid));
            rankMembers.setUserid(Long.parseLong(userid));
            List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,null,null);
            if (null != rankMemberses && rankMemberses.size() != 0){
                RankMembers rankMembers1 = rankMemberses.get(0);
                rankMembers1.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(rankMembers1.getUserid())));
                baseResp = BaseResp.ok();
                baseResp.setData(rankMemberses.get(0));
            }
        } catch (NumberFormatException e) {
            logger.error("select rankmember info is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<List<RankMembers>> selectRankMemberListForApp(String rankid, Integer startNo, Integer pageSize) {
        BaseResp<List<RankMembers>> baseResp = new BaseResp<>();
        RankMembers rankMembers = new RankMembers();
        try {
            rankMembers.setRankid(Long.parseLong(rankid));
            List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,startNo,pageSize);
            for (RankMembers rankMembers1 : rankMemberses){
                rankMembers1.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(rankMembers1.getUserid())));
            }
            baseResp = BaseResp.ok();
            baseResp.setData(rankMemberses);
        } catch (NumberFormatException e) {
            logger.error("select rankmember list rankid={} is error:",rankid,e);
        }
        return baseResp;
    }



}
