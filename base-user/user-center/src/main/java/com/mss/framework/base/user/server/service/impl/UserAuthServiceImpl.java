package com.mss.framework.base.user.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.user.server.dao.UserAuthMapper;
import com.mss.framework.base.user.server.dto.UserAuthDTO;
import com.mss.framework.base.server.user.po.UserAuth;
import com.mss.framework.base.user.server.service.IUserAuthService;
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
 * @Title: UserAuthServiceImpl
 * @Description: UserAuth业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Service
public class UserAuthServiceImpl implements IUserAuthService {

    @Autowired
    private UserAuthMapper userAuthMapper;

    /**
     * @Title: list
     * @Description: 查询UserAuth列表
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
        UserAuth userAuth = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            userAuth = JSON.parseObject(JSON.toJSONString(params), UserAuth.class);
        }
        List<UserAuth> userAuthList = userAuthMapper.selectPageList(userAuth);
        List<UserAuthDTO> userAuthDTOList = new ArrayList<>();
        BeanUtils.copyProperties(userAuthList, userAuthDTOList, List.class);
        PageInfo pageInfo = new PageInfo(userAuthList);
        pageInfo.setList(userAuthDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * @Title: select
     * @Description: 查询UserAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<UserAuthDTO>
     */
    @Override
    public ServerResponse<UserAuthDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        UserAuth userAuth = userAuthMapper.selectByPrimaryKey(id);
        if (userAuth == null) {
            return ServerResponse.createByErrorMessage("UserAuth不存在");
        }
        UserAuthDTO userAuthDTO = new UserAuthDTO();
        BeanUtils.copyProperties(userAuth, userAuthDTO);
        return ServerResponse.createBySuccess(userAuthDTO);
    }
    /**
     * @Title: insert
     * @Description: 保存UserAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param userAuthDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> insert(UserAuthDTO userAuthDTO) {
        UserAuth userAuth = new UserAuth();
        BeanUtils.copyProperties(userAuthDTO, userAuth);
        int rowCount = userAuthMapper.insertSelective(userAuth);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增UserAuth失败");
        }
        return ServerResponse.createBySuccessMessage("新增UserAuth成功");
    }
    /**
     * @Title: update
     * @Description: 更新UserAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param userAuthDTO
     * @return ServerResponse<String>
     */
    @Override
    public ServerResponse<String> update(String id, UserAuthDTO userAuthDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        userAuthDTO.setId(id);
        UserAuth userAuth = new UserAuth();
        BeanUtils.copyProperties(userAuthDTO, userAuth);
        int rowCount = userAuthMapper.updateByPrimaryKeySelective(userAuth);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新UserAuth失败");
        }
        return ServerResponse.createBySuccessMessage("更新UserAuth成功");
    }
    /**
     * @Title: delete
     * @Description: 批量删除UserAuth对象
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
        int rowCount = userAuthMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除UserAuth失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除UserAuth成功");
    }
}
