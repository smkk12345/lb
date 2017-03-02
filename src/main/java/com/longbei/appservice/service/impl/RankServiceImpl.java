package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.dao.RankAwardMapper;
import com.longbei.appservice.dao.RankCheckDetailMapper;
import com.longbei.appservice.dao.RankImageMapper;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankAward;
import com.longbei.appservice.entity.RankCheckDetail;
import com.longbei.appservice.entity.RankImage;
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

    private boolean insertRankAward(String rankid, List<RankAward> rankAwards){
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
        } catch (Exception e) {
            logger.error("update rank:{} is error:{}", JSONObject.fromObject(rankImage),e);
        }
        return res != 0;
    }

    @Override
    public BaseResp<RankImage> selectRankImage(String rankimageid) {
        try {
            RankImage rankImage = rankImageMapper.selectByRankImageId(rankimageid);
            BaseResp<RankImage> baseResp = BaseResp.ok();
            baseResp.setData(rankImage);
            return baseResp;
        } catch (Exception e) {
            logger.error("select rank image by rankimageid={} is error:{}",rankimageid,e);
        }
        return BaseResp.fail();
    }

    @Override
    public BaseResp publishRankImage(String rankimageid) {
        BaseResp<RankImage> baseResp = selectRankImage(rankimageid);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }
        RankImage rankImage = baseResp.getData();
        if (!Constant.RANKIMAGE_STATUS_4.equals(rankImage.getCheckstatus())){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_60, Constant.RTNINFO_SYS_60);
        }
        Rank rank = new Rank();
        try {
            BeanUtils.copyProperties(rank,rankImage);
        } catch (Exception e) {
            logger.error("copy rankimage to rank is error:{}",e);
        }
        try {
            Rank rank1 = rankMapper.selectByPrimaryKey(Long.parseLong(rankimageid));
            int res = 0;
            if (null != rank1){
                res = rankMapper.updateByPrimaryKeySelective(rank);
            } else {
                res = rankMapper.insertSelective(rank);
            }

            if (res > 0){
                return BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("publish rank rankid={} is error:",rankimageid,e);
        }
        return BaseResp.fail();

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
        boolean flag = updateRankImage(rankImage);
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

}
