package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserCertify;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCertifyMapper {
    int insertCertify(UserCertify data);

    UserCertify selectCertifyById(@Param("id") int id);

    int updateApplyCertify(UserCertify data);
}