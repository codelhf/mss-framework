package com.mss.framework.base.user.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.user.server.common.ResponseCode;
import com.mss.framework.base.user.server.common.ServerResponse;
import com.mss.framework.base.user.server.dao.UserMapper;
import com.mss.framework.base.user.server.dto.UserDTO;
import com.mss.framework.base.user.server.enums.ScopeEnum;
import com.mss.framework.base.user.server.pojo.User;
import com.mss.framework.base.user.server.service.IUserService;
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
 * @Title: UserServiceImpl
 * @Description: User业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * @Title: list
     * @Description: 查询User列表
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
        User user = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            user = JSON.parseObject(JSON.toJSONString(params), User.class);
        }
        List<User> userList = userMapper.selectPageList(user);
        List<UserDTO> userDTOList = new ArrayList<>();
        BeanUtils.copyProperties(userList, userDTOList, List.class);
        PageInfo pageInfo = new PageInfo(userList);
        pageInfo.setList(userDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询User对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<UserDTO>
     */
    @Override
    public ServerResponse<UserDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null) {
            return ServerResponse.createByErrorMessage("User不存在");
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return ServerResponse.createBySuccess(userDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存User对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param userDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        int rowCount = userMapper.insertSelective(user);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增User失败");
        }
        return ServerResponse.createBySuccessMessage("新增User成功");
    }
    /**
     * @Title: update
     * @Description: 更新User对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param userDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, UserDTO userDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        userDTO.setId(id);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        int rowCount = userMapper.updateByPrimaryKeySelective(user);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新User失败");
        }
        return ServerResponse.createBySuccessMessage("更新User成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除User对象
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
        int rowCount = userMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除User失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除User成功");
    }

    @Override
    public User selectUserInfoByScope(String userId, String scope) {
        User user = userMapper.selectByPrimaryKey(userId);
        //如果是基础权限，则部分信息不返回
        if (ScopeEnum.BASIC.getCode().equalsIgnoreCase(scope)) {
            user.setPhone(null);
            user.setEmail(null);
            user.setBirthday(null);
            user.setCreateTime(null);
            user.setUpdateTime(null);
        }
        return user;
    }

    @Override
    public User selectByUserId(String id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
