package com.mss.framework.base.user.server.service.manage.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dao.OAuthClientUserMapper;
import com.mss.framework.base.user.server.dto.OAuthClientUserDTO;
import com.mss.framework.base.user.server.pojo.OAuthClientUser;
import com.mss.framework.base.user.server.service.manage.IOAuthClientUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: OAuthClientUserServiceImpl
 * @Description: OAuthClientUser业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Service
public class OAuthClientUserServiceImpl implements IOAuthClientUserService {

    @Autowired
    private OAuthClientUserMapper oAuthClientUserMapper;

    /**
     * @Title: list
     * @Description: 查询OAuthClientUser列表
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
        OAuthClientUser oAuthClientUser = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            oAuthClientUser = JSON.parseObject(JSON.toJSONString(params), OAuthClientUser.class);
        }
        List<OAuthClientUser> oAuthClientUserList = oAuthClientUserMapper.selectPageList(oAuthClientUser);
        List<OAuthClientUserDTO> oAuthClientUserDTOList = new ArrayList<>();
        BeanUtils.copyProperties(oAuthClientUserList, oAuthClientUserDTOList, List.class);
        PageInfo pageInfo = new PageInfo(oAuthClientUserList);
        pageInfo.setList(oAuthClientUserDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询OAuthClientUser对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<OAuthClientUserDTO>
     */
    @Override
    public ServerResponse<OAuthClientUserDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        OAuthClientUser oAuthClientUser = oAuthClientUserMapper.selectByPrimaryKey(id);
        if (oAuthClientUser == null) {
            return ServerResponse.createByErrorMessage("OAuthClientUser不存在");
        }
        OAuthClientUserDTO oAuthClientUserDTO = new OAuthClientUserDTO();
        BeanUtils.copyProperties(oAuthClientUser, oAuthClientUserDTO);
        return ServerResponse.createBySuccess(oAuthClientUserDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存OAuthClientUser对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param oAuthClientUserDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(OAuthClientUserDTO oAuthClientUserDTO) {
        OAuthClientUser oAuthClientUser = new OAuthClientUser();
        BeanUtils.copyProperties(oAuthClientUserDTO, oAuthClientUser);
        int rowCount = oAuthClientUserMapper.insertSelective(oAuthClientUser);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增OAuthClientUser失败");
        }
        return ServerResponse.createBySuccessMessage("新增OAuthClientUser成功");
    }
    /**
     * @Title: update
     * @Description: 更新OAuthClientUser对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param oAuthClientUserDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, OAuthClientUserDTO oAuthClientUserDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        oAuthClientUserDTO.setId(id);
        OAuthClientUser oAuthClientUser = new OAuthClientUser();
        BeanUtils.copyProperties(oAuthClientUserDTO, oAuthClientUser);
        int rowCount = oAuthClientUserMapper.updateByPrimaryKeySelective(oAuthClientUser);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新OAuthClientUser失败");
        }
        return ServerResponse.createBySuccessMessage("更新OAuthClientUser成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除OAuthClientUser对象
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
        int rowCount = oAuthClientUserMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除OAuthClientUser失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除OAuthClientUser成功");
    }
}
