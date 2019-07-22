package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.OAuthAppUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: OAuthAppUserMapper
 * @Description: OAuthClientUser实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Mapper
public interface OAuthAppUserMapper {

    OAuthAppUser selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(OAuthAppUser record);

    int insertSelective(OAuthAppUser record);

    int updateByPrimaryKeySelective(OAuthAppUser record);

    int updateByPrimaryKey(OAuthAppUser record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<OAuthAppUser> selectPageList(OAuthAppUser example);

    OAuthAppUser selectByExample(@Param("userId") String userId, @Param("appId") String appId, @Param("scopeId") String scopeId);
}