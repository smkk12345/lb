package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.RankService;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.collection.immutable.Stream;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 榜单操作接口实现类
 *
 * @author luye
 * @create 2017-01-20 下午3:29
 **/
@Service("rankService")
public class RankServiceImpl implements RankService{

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

    @Override
    public BaseResp<Rank> selectRankDetailByRankid(String rankid) {
        BaseResp<Rank> baseResp = new BaseResp();
        try {
            Rank rank = rankMapper.selectRankByRankid(Long.parseLong(rankid));
            if (null != rank){
                rank.setRankAwards(selectRankAwardByRankidRelease(String.valueOf(rankid)));
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

    private List<RankAwardRelease> selectRankAwardByRankidRelease(String rankid){
        List<RankAwardRelease> rankAwards = rankAwardReleaseMapper.selectListByRankid(rankid);
        for (RankAwardRelease rankAward : rankAwards){
            Award award = awardMapper.selectByPrimaryKey(Integer.parseInt(rankAward.getAwardid()));
            rankAward.setAward(award);
        }
        return rankAwards;
    }

}
