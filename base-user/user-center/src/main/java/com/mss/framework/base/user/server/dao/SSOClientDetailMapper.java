package com.mss.framework.base.user.server.dao;

import com.mss.framework.base.user.server.pojo.SSOClientDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: SSOClientDetailMapper
 * @Description: SSOClientDetail实体类
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Mapper
public interface SSOClientDetailMapper  {

    SSOClientDetail selectByPrimaryKey(String id);
    
    int deleteByPrimaryKey(String id);

    int insert(SSOClientDetail record);

    int insertSelective(SSOClientDetail record);

    int updateByPrimaryKeySelective(SSOClientDetail record);

    int updateByPrimaryKey(SSOClientDetail record);

    int deleteByIdList(@Param("idList") List<String> idList);
    
    List<SSOClientDetail> selectPageList(SSOClientDetail example);

    SSOClientDetail selectByRedirectUrl(String redirectUrl);
}