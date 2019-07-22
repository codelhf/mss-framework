package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.SSOAccessToken;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: SSOAccessTokenMapper
 * @Description: SSOAccessToken实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
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

    SSOAccessToken selectByAccessToken(String accessToken);

    SSOAccessToken selectByUserIdAppId(@Param("userId") String userId, @Param("appId") String appId);
}