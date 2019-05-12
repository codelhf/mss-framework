package com.mss.framework.base.user.server.service.manage.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.user.server.dao.SSORefreshTokenMapper;
import com.mss.framework.base.user.server.dto.SSORefreshTokenDTO;
import com.mss.framework.base.user.server.pojo.SSORefreshToken;
import com.mss.framework.base.user.server.service.manage.ISSORefreshTokenService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: SSORefreshTokenServiceImpl
 * @Description: SSORefreshToken业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Service
public class SSORefreshTokenServiceImpl implements ISSORefreshTokenService {

    @Autowired
    private SSORefreshTokenMapper sSORefreshTokenMapper;

    /**
     * @Title: list
     * @Description: 查询SSORefreshToken列表
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param pageNum
     * @param pageSize
     * @param params
     * @return ServerResponse<PageInfo>
     */
    @Override
    public ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Map<String, String> params) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        SSORefreshToken sSORefreshToken = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            sSORefreshToken = JSON.parseObject(JSON.toJSONString(params), SSORefreshToken.class);
        }
        List<SSORefreshToken> sSORefreshTokenList = sSORefreshTokenMapper.selectPageList(sSORefreshToken);
        List<SSORefreshTokenDTO> sSORefreshTokenDTOList = new ArrayList<>();
        BeanUtils.copyProperties(sSORefreshTokenList, sSORefreshTokenDTOList, List.class);
        PageInfo pageInfo = new PageInfo(sSORefreshTokenList);
        pageInfo.setList(sSORefreshTokenDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询SSORefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @return ServerResponse<SSORefreshTokenDTO>
     */
    @Override
    public ServerResponse<SSORefreshTokenDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        SSORefreshToken sSORefreshToken = sSORefreshTokenMapper.selectByPrimaryKey(id);
        if (sSORefreshToken == null) {
            return ServerResponse.createByErrorMessage("SSORefreshToken不存在");
        }
        SSORefreshTokenDTO sSORefreshTokenDTO = new SSORefreshTokenDTO();
        BeanUtils.copyProperties(sSORefreshToken, sSORefreshTokenDTO);
        return ServerResponse.createBySuccess(sSORefreshTokenDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存SSORefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param sSORefreshTokenDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(SSORefreshTokenDTO sSORefreshTokenDTO) {
        SSORefreshToken sSORefreshToken = new SSORefreshToken();
        BeanUtils.copyProperties(sSORefreshTokenDTO, sSORefreshToken);
        int rowCount = sSORefreshTokenMapper.insertSelective(sSORefreshToken);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增SSORefreshToken失败");
        }
        return ServerResponse.createBySuccessMessage("新增SSORefreshToken成功");
    }
    /**
     * @Title: update
     * @Description: 更新SSORefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @param sSORefreshTokenDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, SSORefreshTokenDTO sSORefreshTokenDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        sSORefreshTokenDTO.setId(id);
        SSORefreshToken sSORefreshToken = new SSORefreshToken();
        BeanUtils.copyProperties(sSORefreshTokenDTO, sSORefreshToken);
        int rowCount = sSORefreshTokenMapper.updateByPrimaryKeySelective(sSORefreshToken);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新SSORefreshToken失败");
        }
        return ServerResponse.createBySuccessMessage("更新SSORefreshToken成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除SSORefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param ids
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> delete(String ids) {
        List<String> idList = Splitter.on(",").splitToList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "id不能为空");
        }
        int rowCount = sSORefreshTokenMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除SSORefreshToken失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除SSORefreshToken成功");
    }
}
