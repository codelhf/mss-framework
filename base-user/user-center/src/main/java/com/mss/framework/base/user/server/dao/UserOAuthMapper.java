package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.UserOAuth;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: UserOAuthMapper
 * @Description: UserOAuth持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-26 21:51:42
 */
@Mapper
public interface UserOAuthMapper  {

    UserOAuth selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(UserOAuth record);

    int insertSelective(UserOAuth record);

    int updateByPrimaryKeySelective(UserOAuth record);

    int updateByPrimaryKey(UserOAuth record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<UserOAuth> selectPageList(UserOAuth example);
}