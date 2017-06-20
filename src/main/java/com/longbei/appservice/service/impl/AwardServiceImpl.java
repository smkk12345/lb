package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.dao.AwardClassifyMapper;
import com.longbei.appservice.dao.AwardMapper;
import com.longbei.appservice.entity.Award;
import com.longbei.appservice.entity.AwardClassify;
import com.longbei.appservice.service.AwardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 奖品相关操作实现类
 *
 * @author luye
 * @create 2017-02-27 下午4:09
 **/
@Service
public class AwardServiceImpl implements AwardService {

    private static Logger logger = LoggerFactory.getLogger(AwardServiceImpl.class);

    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private AwardClassifyMapper awardClassifyMapper;
    @Autowired
    private IdGenerateService idGenerateService;


    @Override
    public boolean insertAward(Award award) {
        try {
            award.setId(idGenerateService.getUniqueIdAsLong());
            Date date = new Date();
            award.setCreatetime(date);
            award.setUpdatetime(date);
            int res = awardMapper.insertSelective(award);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("insert award is error:{}",e);
        }
        return false;
    }

    @Override
    public boolean updateAward(Award award) {
        try {
            Date date = new Date();
            award.setUpdatetime(date);
            int res = awardMapper.updateByPrimaryKeySelective(award);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("update award is error:{}",e);
        }
        return false;
    }

    @Override
    public boolean deleteAward(Long id) {
        try {
            int res = awardMapper.deleteByPrimaryKey(id);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("delete award is error:{}",e);
        }
        return false;
    }

    @Override
    public Page<Award> selectAwardListWithPage(Award award, int pageno, int pagesize) {

        Page<Award> page = new Page<>(pageno,pagesize);
        try {
            int totalcount = awardMapper.selectAwardCount(award);
            List<Award> awards = awardMapper.selectAwardList(award,pagesize*(pageno-1),pagesize);
            page.setTotalCount(totalcount);
            page.setList(awards);
            return page;
        } catch (Exception e) {
            logger.error("select award list with page is error:{}",e);
        }
        return page;
    }

    @Override
    public Page<AwardClassify> selectAwardClassifyListWithPage(int pageno, int pagesize) {
        Page<AwardClassify> page = new Page<>(pageno,pagesize);
        try {
            int totalcount = awardClassifyMapper.selectAwardClassifyCount();
            List<AwardClassify> awardClassifies = awardClassifyMapper.selectAwardClassifyList(pagesize*(pageno-1),pagesize);
            page.setTotalCount(totalcount);
            page.setList(awardClassifies);
            return page;
        } catch (Exception e) {
            logger.error("select awardClassifies list with page is error:{}",e);
        }
        return page;
    }

    @Override
    public List<Award> selectAwardList(Award award) {
        List<Award> awards = null;
        try {
            awards = awardMapper.selectAwardList(award,null,null);
        } catch (Exception e) {
            logger.error("select award list is error:{}",e);
        }
        return awards;
    }

    @Override
    public List<AwardClassify> selectAwardClassifyList() {
        List<AwardClassify> awardClassifies = null;
        try {
            awardClassifies = awardClassifyMapper.selectAwardClassifyList(null,null);
        } catch (Exception e) {
            logger.error("select awardClassify list is error:{}",e);
        }
        return awardClassifies;
    }

    @Override
    public Award selectAward(String awardid) {
        Award award = null;
        try {
            award = awardMapper.selectByPrimaryKey(Long.valueOf(awardid));
        } catch (NumberFormatException e) {
            logger.error("select award awardid={} is error:{}",awardid,e);
        }
        return award;
    }

    @Override
    public boolean insertAwardClassify(AwardClassify awardClassify) {
        try {
            Date date = new Date();
            awardClassify.setCreatetime(date);
            awardClassify.setUpdatetime(date);
            int res = awardClassifyMapper.insertSelective(awardClassify);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("insert awardclassify is error:{}",e);
        }
        return false;
    }

    @Override
    public boolean updateAwardClassify(AwardClassify awardClassify) {
        try {
            Date date = new Date();
            awardClassify.setUpdatetime(date);
            int res = awardClassifyMapper.updateByPrimaryKeySelective(awardClassify);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("update awardclassify is error:{}",e);
        }
        return false;
    }

    @Override
    public boolean hasAwards(String classifyid) {
        try {
            int res = awardMapper.awardCountsUnderClassify(Integer.parseInt(classifyid));
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("select awardCounts UnderClassify is error:{}",e);
        }
        return false;
    }

    @Override
    public boolean deleteAwardClassify(Integer id) {
        try {
            int res = awardClassifyMapper.deleteByPrimaryKey(id);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("delete awardclassify is error:{}",e);
        }
        return false;
    }

    @Override
    public AwardClassify selectAwardClassify(Integer id) {
        AwardClassify awardClassify = null;
        try {
            awardClassify = awardClassifyMapper.selectByPrimaryKey(id);
        } catch (NumberFormatException e) {
            logger.error("select awardclassify awardclassifyid={} is error:{}",id,e);
        }
        return awardClassify;
    }
}
