package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ClassroomCertify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserClassroomCertifyMapper {
    int deleteByPrimaryKey(Integer id);

    List<ClassroomCertify> selectClassroomCertifyList(@Param("classroomCertify") ClassroomCertify ClassroomCertify,
                                                      @Param("startNum") Integer startNum,
                                                      @Param("pageSize") Integer pageSize);

    int selectClassroomCertifyListCount(@Param("classroomCertify") ClassroomCertify ClassroomCertify);

    ClassroomCertify selectClassroomCertifyByUserid(@Param("userid")Long userid);

    int insertClassroomCertify(ClassroomCertify record);

    int updateClassroomCertifyByUserid(ClassroomCertify record);
}