package com.mss.framework.base.user.server.service.manage.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dao.UserOAuthMapper;
import com.mss.framework.base.user.server.dto.UserOAuthDTO;
import com.mss.framework.base.user.server.pojo.UserOAuth;
import com.mss.framework.base.user.server.service.manage.UserOAuthService;
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
* @Title: UserOAuthServiceImpl
* @Description: UserOAuth业务层
* @Company: example
* @Author: liuhf
* @CreateTime: 2019-05-15 15:24:33
*/
@Service
public class UserOAuthServiceImpl implements UserOAuthService {

    @Autowired
    private UserOAuthMapper userOAuthMapper;

    /**
     * @Title: list
     * @Description: 查询UserOAuth列表
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-15 15:24:33
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
        UserOAuth userOAuth = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            userOAuth = JSON.parseObject(JSON.toJSONString(params), UserOAuth.class);
        }
        List<UserOAuth> userOAuthList = userOAuthMapper.selectPageList(userOAuth);
        List<UserOAuthDTO> userOAuthDTOList = new ArrayList<>();
        BeanUtils.copyProperties(userOAuthList, userOAuthDTOList, List.class);
        PageInfo pageInfo = new PageInfo(userOAuthList);
        pageInfo.setList(userOAuthDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询UserOAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-15 15:24:33
     *
     * @param id
     * @return ServerResponse<UserOAuthDTO>
     */
    @Override
    public ServerResponse<UserOAuthDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        UserOAuth userOAuth = userOAuthMapper.selectByPrimaryKey(id);
        if (userOAuth == null) {
            return ServerResponse.createByErrorMessage("UserOAuth不存在");
        }
        UserOAuthDTO userOAuthDTO = new UserOAuthDTO();
        BeanUtils.copyProperties(userOAuth, userOAuthDTO);
        return ServerResponse.createBySuccess(userOAuthDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存UserOAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-15 15:24:33
     *
     * @param userOAuthDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(UserOAuthDTO userOAuthDTO) {
        UserOAuth userOAuth = new UserOAuth();
        BeanUtils.copyProperties(userOAuthDTO, userOAuth);
        int rowCount = userOAuthMapper.insertSelective(userOAuth);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增UserOAuth失败");
        }
        return ServerResponse.createBySuccessMessage("新增UserOAuth成功");
    }
    /**
     * @Title: update
     * @Description: 更新UserOAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-15 15:24:33
     *
     * @param id
     * @param userOAuthDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, UserOAuthDTO userOAuthDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        userOAuthDTO.setId(id);
        UserOAuth userOAuth = new UserOAuth();
        BeanUtils.copyProperties(userOAuthDTO, userOAuth);
        int rowCount = userOAuthMapper.updateByPrimaryKeySelective(userOAuth);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新UserOAuth失败");
        }
        return ServerResponse.createBySuccessMessage("更新UserOAuth成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除UserOAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-15 15:24:33
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
        int rowCount = userOAuthMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除UserOAuth失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除UserOAuth成功");
    }
}
