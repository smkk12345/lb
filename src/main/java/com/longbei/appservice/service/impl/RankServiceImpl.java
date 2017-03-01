package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.dao.RankCheckDetailMapper;
import com.longbei.appservice.dao.RankImageMapper;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.entity.Rank;
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

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public boolean insertRank(String rankdetail, String ranktitle,
                              int ranklimite, String rankscope,
                              String rankphotos, double rankrate,
                              Date starttime, Date endtime,
                              String areaname, String createuserid,
                              String ranktype, String ispublic,
                              String rankcateid, int likescore,
                              int flowerscore, int diamondscore,
                              String codeword, String ptype,
                              String sourcetype, String companyname,
                              String companyphotos, String companybrief) {

        RankImage rankImage = new RankImage();

        rankImage.setRankid(new Date().getTime());
        rankImage.setRanktitle(ranktitle);
        rankImage.setRankdetail(rankdetail);
        rankImage.setRankphotos(rankphotos);
        rankImage.setRanklimite(ranklimite);
        rankImage.setRankrate(Double.valueOf(rankrate));
        rankImage.setRankscope(rankscope);
        rankImage.setStarttime(starttime);
        rankImage.setEndtime(endtime);
        rankImage.setAreaname(areaname);
        rankImage.setCreateuserid(Long.parseLong(createuserid));
        rankImage.setRanktype(ranktype);
        rankImage.setIspublic(ispublic);
        rankImage.setRankcateid(Integer.parseInt(rankcateid));
        rankImage.setLikescore(likescore);
        rankImage.setFlowerscore(flowerscore);
        rankImage.setDiamondscore(diamondscore);
        rankImage.setCodeword(codeword);
        rankImage.setPtype(ptype);
        rankImage.setSourcetype(sourcetype);
        rankImage.setCompanyname(companyname);
        rankImage.setCompanyphotos(companyphotos);
        rankImage.setCompanybrief(companybrief);
        rankImage.setCreatetime(new Date());

        int res = 0;
        try {
            res = rankImageMapper.insertSelective(rankImage);
        } catch (Exception e) {
            logger.error("insert rank:{} is error:{}", JSONObject.fromObject(rankImage),e);
        }
        return res != 0;
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
        int res = rankMapper.insertSelective(rank);
        if (res > 0){
            return baseResp.ok();
        }
        return BaseResp.fail();

    }

    @Override
    public Page<RankImage> selectRankImageList(RankImage rankImage,int pageno, int pagesize) {
        int totalcount = rankImageMapper.selectListCount(new RankImage());
        pageno = Page.setPageNo(pageno,totalcount,pagesize);
        List<RankImage> rankImages = rankImageMapper.selectListWithPage(rankImage,(pageno-1)*pagesize,pagesize);
        Page<RankImage> page = new Page<RankImage>(pageno,pagesize,totalcount,rankImages);
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
    public boolean updateSponsornumAndSponsormoney( ) {
        int res = 0;
        try {
            res = rankMapper.updateSponsornumAndSponsormoney();
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
                if ("1".equals(rankImage.getIsauto())){
                    publishRankImage(String.valueOf(rankCheckDetail.getRankid()));
                }
            }
        }
        //神格不通过 不可以修改 直接删除
        if (Constant.RANKIMAGE_STATUS_3.equals(rankCheckDetail.getCheckstatus())){
            deleteRankImage(String.valueOf(rankCheckDetail.getRankid()));
        }
>>>>>>> a2a2a745f3e103d63e7de4ca9295850b1f4e76a4
    }
}
