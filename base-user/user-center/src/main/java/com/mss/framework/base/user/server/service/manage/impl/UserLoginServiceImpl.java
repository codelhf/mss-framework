package com.mss.framework.base.user.server.service.manage.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dao.UserLoginMapper;
import com.mss.framework.base.user.server.dto.UserLoginDTO;
import com.mss.framework.base.user.server.pojo.UserLogin;
import com.mss.framework.base.user.server.service.manage.UserLoginService;
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
* @Title: UserLoginServiceImpl
* @Description: UserLogin业务层
* @Company: example
* @Author: liuhf
* @CreateTime: 2019-05-15 15:24:33
*/
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserLoginMapper userLoginMapper;

    /**
     * @Title: list
     * @Description: 查询UserLogin列表
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
        UserLogin userLogin = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            userLogin = JSON.parseObject(JSON.toJSONString(params), UserLogin.class);
        }
        List<UserLogin> userLoginList = userLoginMapper.selectPageList(userLogin);
        List<UserLoginDTO> userLoginDTOList = new ArrayList<>();
        BeanUtils.copyProperties(userLoginList, userLoginDTOList, List.class);
        PageInfo pageInfo = new PageInfo(userLoginList);
        pageInfo.setList(userLoginDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询UserLogin对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-15 15:24:33
     *
     * @param id
     * @return ServerResponse<UserLoginDTO>
     */
    @Override
    public ServerResponse<UserLoginDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        UserLogin userLogin = userLoginMapper.selectByPrimaryKey(id);
        if (userLogin == null) {
            return ServerResponse.createByErrorMessage("UserLogin不存在");
        }
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        BeanUtils.copyProperties(userLogin, userLoginDTO);
        return ServerResponse.createBySuccess(userLoginDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存UserLogin对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-15 15:24:33
     *
     * @param userLoginDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(UserLoginDTO userLoginDTO) {
        UserLogin userLogin = new UserLogin();
        BeanUtils.copyProperties(userLoginDTO, userLogin);
        int rowCount = userLoginMapper.insertSelective(userLogin);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增UserLogin失败");
        }
        return ServerResponse.createBySuccessMessage("新增UserLogin成功");
    }
    /**
     * @Title: update
     * @Description: 更新UserLogin对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-15 15:24:33
     *
     * @param id
     * @param userLoginDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, UserLoginDTO userLoginDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        userLoginDTO.setId(id);
        UserLogin userLogin = new UserLogin();
        BeanUtils.copyProperties(userLoginDTO, userLogin);
        int rowCount = userLoginMapper.updateByPrimaryKeySelective(userLogin);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新UserLogin失败");
        }
        return ServerResponse.createBySuccessMessage("更新UserLogin成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除UserLogin对象
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
        int rowCount = userLoginMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除UserLogin失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除UserLogin成功");
    }
}
