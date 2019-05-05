package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.po.SSORefreshToken;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: SSORefreshTokenMapper
 * @Description: SSORefreshToken实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
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

    SSORefreshToken selectByTokenId(String tokenId);

    SSORefreshToken selectByRefreshToken(String refreshToken);
}