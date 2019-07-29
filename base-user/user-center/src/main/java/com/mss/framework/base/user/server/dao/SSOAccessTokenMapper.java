package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.SSOAccessToken;
import com.mss.framework.base.user.server.pojo.SSORefreshToken;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: SSOAccessTokenMapper
 * @Description: SSOAccessToken持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-07-29 00:33:05
 */
@Mapper
public interface SSOAccessTokenMapper  {

    SSOAccessToken selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(SSOAccessToken record);

    int insertSelective(SSOAccessToken record);

    int updateByPrimaryKeySelective(SSOAccessToken record);

    int updateByPrimaryKey(SSOAccessToken record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<SSOAccessToken> selectPageList(SSOAccessToken example);

    SSOAccessToken selectByAccessToken(@Param("accessToken") String accessToken);

    SSOAccessToken selectByUserIdClientId(@Param("userId") String userId, @Param("clientId") String clientId);
}