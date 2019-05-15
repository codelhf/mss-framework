package com.mss.framework.base.user.server.service.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.UserLoginDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: UserLoginService
 * @Description: UserLogin接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-15 15:24:33
 */
public interface UserLoginService {

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
	ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Map<String, String> params);

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
	ServerResponse<UserLoginDTO> select(String id);

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
	ServerResponse<String> insert(UserLoginDTO userLoginDTO);

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
	ServerResponse<String> update(String id, UserLoginDTO userLoginDTO);

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
	ServerResponse<String> delete(String ids);
}
