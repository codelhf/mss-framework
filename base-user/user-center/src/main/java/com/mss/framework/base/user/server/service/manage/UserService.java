package com.mss.framework.base.user.server.service.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.UserDTO;
import com.github.pagehelper.PageInfo;
import com.mss.framework.base.user.server.pojo.User;

import java.util.Map;

/**
 * @Title: UserService
 * @Description: User接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
public interface UserService {

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
	ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Map<String, String> params);

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
	ServerResponse<UserDTO> select(String id);

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
	ServerResponse<String> insert(UserDTO userDTO);

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
	ServerResponse<String> update(String id, UserDTO userDTO);

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
	ServerResponse<String> delete(String ids);

	/**
	 * @description: 通过scope查询不同程度的用户信息
	 * @author liuhf
	 * @createtime 2019/5/3 23:28
	 *
	 * @param [userId, scope]
	 * @return User
	 */
	User selectUserInfoByScope(String userId, String scope);

	/**
	 * @description: 通过用户ID查询用户信息
	 * @author liuhf
	 * @createtime 2019/5/3 23:30
	 *
	 * @param [id]
	 * @return User
	 */
	User selectByUserId(String id);
}
