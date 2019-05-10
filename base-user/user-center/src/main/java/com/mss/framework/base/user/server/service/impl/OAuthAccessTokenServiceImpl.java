package com.mss.framework.base.user.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.user.server.dao.OAuthAccessTokenMapper;
import com.mss.framework.base.user.server.dto.OAuthAccessTokenDTO;
import com.mss.framework.base.server.user.po.OAuthAccessToken;
import com.mss.framework.base.user.server.service.IOAuthAccessTokenService;
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
 * @Title: OAuthAccessTokenServiceImpl
 * @Description: OAuthAccessToken业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Service
public class OAuthAccessTokenServiceImpl implements IOAuthAccessTokenService {

    @Autowired
    private OAuthAccessTokenMapper oAuthAccessTokenMapper;

    /**
     * @Title: list
     * @Description: 查询OAuthAccessToken列表
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
        OAuthAccessToken oAuthAccessToken = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            oAuthAccessToken = JSON.parseObject(JSON.toJSONString(params), OAuthAccessToken.class);
        }
        List<OAuthAccessToken> oAuthAccessTokenList = oAuthAccessTokenMapper.selectPageList(oAuthAccessToken);
        List<OAuthAccessTokenDTO> oAuthAccessTokenDTOList = new ArrayList<>();
        BeanUtils.copyProperties(oAuthAccessTokenList, oAuthAccessTokenDTOList, List.class);
        PageInfo pageInfo = new PageInfo(oAuthAccessTokenList);
        pageInfo.setList(oAuthAccessTokenDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询OAuthAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<OAuthAccessTokenDTO>
     */
    @Override
    public ServerResponse<OAuthAccessTokenDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        OAuthAccessToken oAuthAccessToken = oAuthAccessTokenMapper.selectByPrimaryKey(id);
        if (oAuthAccessToken == null) {
            return ServerResponse.createByErrorMessage("OAuthAccessToken不存在");
        }
        OAuthAccessTokenDTO oAuthAccessTokenDTO = new OAuthAccessTokenDTO();
        BeanUtils.copyProperties(oAuthAccessToken, oAuthAccessTokenDTO);
        return ServerResponse.createBySuccess(oAuthAccessTokenDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存OAuthAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param oAuthAccessTokenDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(OAuthAccessTokenDTO oAuthAccessTokenDTO) {
        OAuthAccessToken oAuthAccessToken = new OAuthAccessToken();
        BeanUtils.copyProperties(oAuthAccessTokenDTO, oAuthAccessToken);
        int rowCount = oAuthAccessTokenMapper.insertSelective(oAuthAccessToken);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增OAuthAccessToken失败");
        }
        return ServerResponse.createBySuccessMessage("新增OAuthAccessToken成功");
    }
    /**
     * @Title: update
     * @Description: 更新OAuthAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param oAuthAccessTokenDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, OAuthAccessTokenDTO oAuthAccessTokenDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        oAuthAccessTokenDTO.setId(id);
        OAuthAccessToken oAuthAccessToken = new OAuthAccessToken();
        BeanUtils.copyProperties(oAuthAccessTokenDTO, oAuthAccessToken);
        int rowCount = oAuthAccessTokenMapper.updateByPrimaryKeySelective(oAuthAccessToken);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新OAuthAccessToken失败");
        }
        return ServerResponse.createBySuccessMessage("更新OAuthAccessToken成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除OAuthAccessToken对象
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
        int rowCount = oAuthAccessTokenMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除OAuthAccessToken失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除OAuthAccessToken成功");
    }
}
