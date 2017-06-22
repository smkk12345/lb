package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserAccount;

public interface UserAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAccount record);

    /**
     * @Title: insertUserAccount
     * @Description: 添加用户账号冻结
     * @auther IngaWu
     * @currentdate:2017年6月21日
     */
    int insertUserAccount(UserAccount record);

    /**
     * @Title: selectUserAccountByUserId
     * @Description: 查看用户账号冻结详情
     * @auther IngaWu
     * @currentdate:2017年6月21日
     */
    UserAccount selectUserAccountByUserId(Long userId);

    /**
     * 编辑用户账号冻结详情
     * @title updateUserAccountByUserId
     * @author IngaWu
     * @currentdate:2017年6月21日
     */
    int updateUserAccountByUserId(UserAccount record);

    int updateByPrimaryKey(UserAccount record);
}