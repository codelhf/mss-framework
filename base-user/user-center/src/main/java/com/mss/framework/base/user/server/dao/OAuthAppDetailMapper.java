package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.OAuthAppDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: OAuthAppDetailMapper
 * @Description: OAuthClientDetail实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Mapper
public interface OAuthAppDetailMapper {

    OAuthAppDetail selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(OAuthAppDetail record);

    int insertSelective(OAuthAppDetail record);

    int updateByPrimaryKeySelective(OAuthAppDetail record);

    int updateByPrimaryKey(OAuthAppDetail record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<OAuthAppDetail> selectPageList(OAuthAppDetail example);

    OAuthAppDetail selectByAppId(@Param("appId") String appId);
}