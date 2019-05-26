package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.UserLogin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: UserLoginMapper
 * @Description: UserLogin持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-15 15:24:33
 */
@Mapper
public interface UserLoginMapper  {

    UserLogin selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(UserLogin record);

    int insertSelective(UserLogin record);

    int updateByPrimaryKeySelective(UserLogin record);

    int updateByPrimaryKey(UserLogin record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<UserLogin> selectPageList(UserLogin example);

    int selectByUsername(String username);

    int selectByEmail(String email);

    int selectByPhone(String phone);

    UserLogin loginByUsername(@Param("username") String username, @Param("password") String password);

    UserLogin loginByEmail(@Param("email") String email, @Param("password") String password);

    UserLogin loginByPhone(@Param("phone") String phone, @Param("password") String password);

}