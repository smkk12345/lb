package com.longbei.appservice.service.impl;

import com.longbei.appservice.dao.RankImageMapper;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.entity.RankImage;
import com.longbei.appservice.service.RankService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

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
        rankImage.setRankrate(BigDecimal.valueOf(rankrate));
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
    public boolean updateRank(long rankid, String rankdetail, String ranktitle,
                              int ranklimite, String rankscope, String rankphotos,
                              double rankrate, Date starttime, Date endtime,
                              String areaname, String createuserid, String ranktype,
                              String ispublic, String rankcateid, int likescore,
                              int flowerscore, int diamondscore, String codeword,
                              String ptype, String sourcetype, String companyname,
                              String companyphotos, String companybrief) {

        RankImage rankImage = new RankImage();

        rankImage.setRankid(rankid);
        rankImage.setRanktitle(ranktitle);
        rankImage.setRankdetail(rankdetail);
        rankImage.setRankphotos(rankphotos);
        rankImage.setRanklimite(ranklimite);
        rankImage.setRankrate(BigDecimal.valueOf(rankrate));
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
        rankImage.setUpdatetime(new Date());

        int res = 0;
        try {
            res = rankImageMapper.updateByPrimaryKeySelective(rankImage);
        } catch (Exception e) {
            logger.error("update rank:{} is error:{}", JSONObject.fromObject(rankImage),e);
        }
        return res != 0;
    }


}
