package com.mss.framework.base.user.server.service.manage.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dao.SSOAccessTokenMapper;
import com.mss.framework.base.user.server.dto.SSOAccessTokenDTO;
import com.mss.framework.base.user.server.pojo.SSOAccessToken;
import com.mss.framework.base.user.server.service.manage.ISSOAccessTokenService;
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
 * @Title: SSOAccessTokenServiceImpl
 * @Description: SSOAccessToken业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Service
public class SSOAccessTokenServiceImpl implements ISSOAccessTokenService {

    @Autowired
    private SSOAccessTokenMapper sSOAccessTokenMapper;

    /**
     * @Title: list
     * @Description: 查询SSOAccessToken列表
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
        SSOAccessToken sSOAccessToken = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            sSOAccessToken = JSON.parseObject(JSON.toJSONString(params), SSOAccessToken.class);
        }
        List<SSOAccessToken> sSOAccessTokenList = sSOAccessTokenMapper.selectPageList(sSOAccessToken);
        List<SSOAccessTokenDTO> sSOAccessTokenDTOList = new ArrayList<>();
        BeanUtils.copyProperties(sSOAccessTokenList, sSOAccessTokenDTOList, List.class);
        PageInfo pageInfo = new PageInfo(sSOAccessTokenList);
        pageInfo.setList(sSOAccessTokenDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询SSOAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @return ServerResponse<SSOAccessTokenDTO>
     */
    @Override
    public ServerResponse<SSOAccessTokenDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        SSOAccessToken sSOAccessToken = sSOAccessTokenMapper.selectByPrimaryKey(id);
        if (sSOAccessToken == null) {
            return ServerResponse.createByErrorMessage("SSOAccessToken不存在");
        }
        SSOAccessTokenDTO sSOAccessTokenDTO = new SSOAccessTokenDTO();
        BeanUtils.copyProperties(sSOAccessToken, sSOAccessTokenDTO);
        return ServerResponse.createBySuccess(sSOAccessTokenDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存SSOAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param sSOAccessTokenDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(SSOAccessTokenDTO sSOAccessTokenDTO) {
        SSOAccessToken sSOAccessToken = new SSOAccessToken();
        BeanUtils.copyProperties(sSOAccessTokenDTO, sSOAccessToken);
        int rowCount = sSOAccessTokenMapper.insertSelective(sSOAccessToken);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增SSOAccessToken失败");
        }
        return ServerResponse.createBySuccessMessage("新增SSOAccessToken成功");
    }
    /**
     * @Title: update
     * @Description: 更新SSOAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @param sSOAccessTokenDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, SSOAccessTokenDTO sSOAccessTokenDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        sSOAccessTokenDTO.setId(id);
        SSOAccessToken sSOAccessToken = new SSOAccessToken();
        BeanUtils.copyProperties(sSOAccessTokenDTO, sSOAccessToken);
        int rowCount = sSOAccessTokenMapper.updateByPrimaryKeySelective(sSOAccessToken);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新SSOAccessToken失败");
        }
        return ServerResponse.createBySuccessMessage("更新SSOAccessToken成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除SSOAccessToken对象
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
        int rowCount = sSOAccessTokenMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除SSOAccessToken失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除SSOAccessToken成功");
    }
}
