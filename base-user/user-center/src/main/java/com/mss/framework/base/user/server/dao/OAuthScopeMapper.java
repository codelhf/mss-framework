package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.OAuthScope;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: OAuthScopeMapper
 * @Description: OAuthScope持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-07-29 00:33:05
 */
@Mapper
public interface OAuthScopeMapper  {

    OAuthScope selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(OAuthScope record);

    int insertSelective(OAuthScope record);

    int updateByPrimaryKeySelective(OAuthScope record);

    int updateByPrimaryKey(OAuthScope record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<OAuthScope> selectPageList(OAuthScope example);

    OAuthScope selectByScope(@Param("scope") String scope);
}