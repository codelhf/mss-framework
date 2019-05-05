package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.UserAuth;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: UserAuthMapper
 * @Description: UserAuth实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Mapper
public interface UserAuthMapper  {

    UserAuth selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(UserAuth record);

    int insertSelective(UserAuth record);

    int updateByPrimaryKeySelective(UserAuth record);

    int updateByPrimaryKey(UserAuth record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<UserAuth> selectPageList(UserAuth example);
}