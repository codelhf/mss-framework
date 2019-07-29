package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.OAuthAccessToken;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: OAuthAccessTokenMapper
 * @Description: OAuthAccessToken持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-07-29 00:33:05
 */
@Mapper
public interface OAuthAccessTokenMapper  {

    OAuthAccessToken selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(OAuthAccessToken record);

    int insertSelective(OAuthAccessToken record);

    int updateByPrimaryKeySelective(OAuthAccessToken record);

    int updateByPrimaryKey(OAuthAccessToken record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<OAuthAccessToken> selectPageList(OAuthAccessToken example);

    OAuthAccessToken selectByUserIdClientIdScope(@Param("userId") String userId, @Param("clientId") String clientId, @Param("scope") String scope);

    OAuthAccessToken selectByAccessToken(@Param("accessToken") String accessToken);
}