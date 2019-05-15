package com.mss.framework.base.user.server.service.manage.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dao.OAuthRefreshTokenMapper;
import com.mss.framework.base.user.server.dto.OAuthRefreshTokenDTO;
import com.mss.framework.base.user.server.pojo.OAuthRefreshToken;
import com.mss.framework.base.user.server.service.manage.OAuthRefreshTokenService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: OAuthRefreshTokenServiceImpl
 * @Description: OAuthRefreshToken业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Service
public class OAuthRefreshTokenServiceImpl implements OAuthRefreshTokenService {

    @Autowired
    private OAuthRefreshTokenMapper oAuthRefreshTokenMapper;

    /**
     * @Title: list
     * @Description: 查询OAuthRefreshToken列表
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
        OAuthRefreshToken oAuthRefreshToken = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            oAuthRefreshToken = JSON.parseObject(JSON.toJSONString(params), OAuthRefreshToken.class);
        }
        List<OAuthRefreshToken> oAuthRefreshTokenList = oAuthRefreshTokenMapper.selectPageList(oAuthRefreshToken);
        List<OAuthRefreshTokenDTO> oAuthRefreshTokenDTOList = new ArrayList<>();
        BeanUtils.copyProperties(oAuthRefreshTokenList, oAuthRefreshTokenDTOList, List.class);
        PageInfo pageInfo = new PageInfo(oAuthRefreshTokenList);
        pageInfo.setList(oAuthRefreshTokenDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询OAuthRefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<OAuthRefreshTokenDTO>
     */
    @Override
    public ServerResponse<OAuthRefreshTokenDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        OAuthRefreshToken oAuthRefreshToken = oAuthRefreshTokenMapper.selectByPrimaryKey(id);
        if (oAuthRefreshToken == null) {
            return ServerResponse.createByErrorMessage("OAuthRefreshToken不存在");
        }
        OAuthRefreshTokenDTO oAuthRefreshTokenDTO = new OAuthRefreshTokenDTO();
        BeanUtils.copyProperties(oAuthRefreshToken, oAuthRefreshTokenDTO);
        return ServerResponse.createBySuccess(oAuthRefreshTokenDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存OAuthRefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param oAuthRefreshTokenDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(OAuthRefreshTokenDTO oAuthRefreshTokenDTO) {
        OAuthRefreshToken oAuthRefreshToken = new OAuthRefreshToken();
        BeanUtils.copyProperties(oAuthRefreshTokenDTO, oAuthRefreshToken);
        int rowCount = oAuthRefreshTokenMapper.insertSelective(oAuthRefreshToken);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增OAuthRefreshToken失败");
        }
        return ServerResponse.createBySuccessMessage("新增OAuthRefreshToken成功");
    }
    /**
     * @Title: update
     * @Description: 更新OAuthRefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param oAuthRefreshTokenDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, OAuthRefreshTokenDTO oAuthRefreshTokenDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        oAuthRefreshTokenDTO.setId(id);
        OAuthRefreshToken oAuthRefreshToken = new OAuthRefreshToken();
        BeanUtils.copyProperties(oAuthRefreshTokenDTO, oAuthRefreshToken);
        int rowCount = oAuthRefreshTokenMapper.updateByPrimaryKeySelective(oAuthRefreshToken);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新OAuthRefreshToken失败");
        }
        return ServerResponse.createBySuccessMessage("更新OAuthRefreshToken成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除OAuthRefreshToken对象
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
        int rowCount = oAuthRefreshTokenMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除OAuthRefreshToken失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除OAuthRefreshToken成功");
    }
}
