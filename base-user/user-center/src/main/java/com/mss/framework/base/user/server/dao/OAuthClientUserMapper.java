package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.OAuthClientUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: OAuthClientUserMapper
 * @Description: OAuthClientUser实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Mapper
public interface OAuthClientUserMapper  {

    OAuthClientUser selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(OAuthClientUser record);

    int insertSelective(OAuthClientUser record);

    int updateByPrimaryKeySelective(OAuthClientUser record);

    int updateByPrimaryKey(OAuthClientUser record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<OAuthClientUser> selectPageList(OAuthClientUser example);

    OAuthClientUser selectByExample(@Param("userId") String userId, @Param("clientId") String clientId, @Param("scopeId") String scopeId);
}