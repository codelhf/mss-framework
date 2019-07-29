package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.SSORefreshToken;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: SSORefreshTokenMapper
 * @Description: SSORefreshToken持久层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-07-29 00:33:05
 */
@Mapper
public interface SSORefreshTokenMapper  {

    SSORefreshToken selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(SSORefreshToken record);

    int insertSelective(SSORefreshToken record);

    int updateByPrimaryKeySelective(SSORefreshToken record);

    int updateByPrimaryKey(SSORefreshToken record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<SSORefreshToken> selectPageList(SSORefreshToken example);

    SSORefreshToken selectByTokenId(@Param("tokenId") String tokenId);

    SSORefreshToken selectByRefreshToken(@Param("refreshToken") String refreshToken);
}