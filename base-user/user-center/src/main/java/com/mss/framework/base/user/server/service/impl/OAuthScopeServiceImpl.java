package com.mss.framework.base.user.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.user.server.dao.OAuthScopeMapper;
import com.mss.framework.base.user.server.dto.OAuthScopeDTO;
import com.mss.framework.base.server.user.po.OAuthScope;
import com.mss.framework.base.user.server.service.IOAuthScopeService;
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
 * @Title: OAuthScopeServiceImpl
 * @Description: OAuthScope业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Service
public class OAuthScopeServiceImpl implements IOAuthScopeService {

    @Autowired
    private OAuthScopeMapper oAuthScopeMapper;

    /**
     * @Title: list
     * @Description: 查询OAuthScope列表
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
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
        OAuthScope oAuthScope = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            oAuthScope = JSON.parseObject(JSON.toJSONString(params), OAuthScope.class);
        }
        List<OAuthScope> oAuthScopeList = oAuthScopeMapper.selectPageList(oAuthScope);
        List<OAuthScopeDTO> oAuthScopeDTOList = new ArrayList<>();
        BeanUtils.copyProperties(oAuthScopeList, oAuthScopeDTOList, List.class);
        PageInfo pageInfo = new PageInfo(oAuthScopeList);
        pageInfo.setList(oAuthScopeDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询OAuthScope对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<OAuthScopeDTO>
     */
    @Override
    public ServerResponse<OAuthScopeDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        OAuthScope oAuthScope = oAuthScopeMapper.selectByPrimaryKey(id);
        if (oAuthScope == null) {
            return ServerResponse.createByErrorMessage("OAuthScope不存在");
        }
        OAuthScopeDTO oAuthScopeDTO = new OAuthScopeDTO();
        BeanUtils.copyProperties(oAuthScope, oAuthScopeDTO);
        return ServerResponse.createBySuccess(oAuthScopeDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存OAuthScope对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param oAuthScopeDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(OAuthScopeDTO oAuthScopeDTO) {
        OAuthScope oAuthScope = new OAuthScope();
        BeanUtils.copyProperties(oAuthScopeDTO, oAuthScope);
        int rowCount = oAuthScopeMapper.insertSelective(oAuthScope);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增OAuthScope失败");
        }
        return ServerResponse.createBySuccessMessage("新增OAuthScope成功");
    }
    /**
     * @Title: update
     * @Description: 更新OAuthScope对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param oAuthScopeDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, OAuthScopeDTO oAuthScopeDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        oAuthScopeDTO.setId(id);
        OAuthScope oAuthScope = new OAuthScope();
        BeanUtils.copyProperties(oAuthScopeDTO, oAuthScope);
        int rowCount = oAuthScopeMapper.updateByPrimaryKeySelective(oAuthScope);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新OAuthScope失败");
        }
        return ServerResponse.createBySuccessMessage("更新OAuthScope成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除OAuthScope对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
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
        int rowCount = oAuthScopeMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除OAuthScope失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除OAuthScope成功");
    }
}
