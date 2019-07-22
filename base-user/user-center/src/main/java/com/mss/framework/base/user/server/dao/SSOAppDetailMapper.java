package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.SSOAppDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: SSOAppDetailMapper
 * @Description: SSOClientDetail实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Mapper
public interface SSOAppDetailMapper {

    SSOAppDetail selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(SSOAppDetail record);

    int insertSelective(SSOAppDetail record);

    int updateByPrimaryKeySelective(SSOAppDetail record);

    int updateByPrimaryKey(SSOAppDetail record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<SSOAppDetail> selectPageList(SSOAppDetail example);

    SSOAppDetail selectByRedirectUrl(String redirectUrl);
}