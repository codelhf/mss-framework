package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.OAuthRefreshToken;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: OAuthRefreshTokenMapper
 * @Description: OAuthRefreshToken实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Mapper
public interface OAuthRefreshTokenMapper  {

    OAuthRefreshToken selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(OAuthRefreshToken record);

    int insertSelective(OAuthRefreshToken record);

    int updateByPrimaryKeySelective(OAuthRefreshToken record);

    int updateByPrimaryKey(OAuthRefreshToken record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<OAuthRefreshToken> selectPageList(OAuthRefreshToken example);

    OAuthRefreshToken selectByRefreshToken(@Param("refreshToken") String refreshToken);

    OAuthRefreshToken selectByTokenId(@Param("tokenId") String tokenId);
}