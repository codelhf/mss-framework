package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.OAuthClientDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: OAuthClientDetailMapper
 * @Description: OAuthClientDetail实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Mapper
public interface OAuthClientDetailMapper  {

    OAuthClientDetail selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(OAuthClientDetail record);

    int insertSelective(OAuthClientDetail record);

    int updateByPrimaryKeySelective(OAuthClientDetail record);

    int updateByPrimaryKey(OAuthClientDetail record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<OAuthClientDetail> selectPageList(OAuthClientDetail example);

    OAuthClientDetail selectByClientId(String clientId);
}