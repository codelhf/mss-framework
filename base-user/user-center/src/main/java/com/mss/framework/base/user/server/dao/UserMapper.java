package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: UserMapper
 * @Description: User实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Mapper
public interface UserMapper  {

    User selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<User> selectPageList(User example);
}