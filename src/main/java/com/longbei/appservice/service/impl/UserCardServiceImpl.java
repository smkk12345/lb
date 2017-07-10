package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.UserCardMapper;
import com.longbei.appservice.entity.UserCard;
import com.longbei.appservice.service.UserCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userCardService")
public class UserCardServiceImpl implements UserCardService {

    private static Logger logger = LoggerFactory.getLogger(UserCardServiceImpl.class);

    @Autowired
    private UserCardMapper userCardMapper;
    @Autowired
    private UserCardService userCardService;
    @Autowired
    private IdGenerateService idGenerateService;

    public  Page<UserCard> selectUserCardList(UserCard userCard, Integer startNum, Integer pageSize){
        Page<UserCard> page = new Page<>(startNum/pageSize+1,pageSize);
        try {
            int totalcount = userCardMapper.selectUserCardListCount(userCard);
            int pageNo = Page.setPageNo(startNum/pageSize+1,totalcount,pageSize);
            List<UserCard> userCardList = new ArrayList<UserCard>();
            userCardList = userCardMapper.selectUserCardList(userCard,startNum,pageSize);
            page.setTotalCount(totalcount);
            page.setList(userCardList);
        } catch (Exception e) {
            logger.error("selectUserCardList error and msg={}",e);
        }
        return page;
    }

    @Override
    public BaseResp<UserCard> selectUserCardByUserCardId(Long userCardId) {
        BaseResp<UserCard> baseResp = new BaseResp<UserCard>();
        try {
            UserCard userCard = userCardMapper.selectByCardid(userCardId);
            baseResp.setData(userCard);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        catch (Exception e) {
            logger.error("selectUserCardByUserCardId error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> deleteByUserCardId(Long userCardId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            int m = userCardMapper.deleteByUserCardId(userCardId);
            if(m == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteByUserCardId error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertUserCard(UserCard userCard){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        userCard.setCreatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        userCard.setCardid(idGenerateService.getUniqueIdAsLong());
        try {
            int n = userCardMapper.insertUserCard(userCard);
            if(n == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertUserCard error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public 	BaseResp<Object> updateByUserCardId(UserCard userCard) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        userCard.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        try {
            int n = userCardMapper.updateByUserCardId(userCard);
            if(n >= 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateByUserCardId error and msg={}",e);
        }
        return baseResp;
    }
}
