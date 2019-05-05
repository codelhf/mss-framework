package com.mss.framework.base.user.server.service;

import com.mss.framework.base.user.server.common.ServerResponse;
import com.mss.framework.base.user.server.dto.UserAuthDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: UserAuthService
 * @Description: UserAuth接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
public interface IUserAuthService {

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
	ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Map<String, String> params);

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
	ServerResponse<UserAuthDTO> select(String id);

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
	ServerResponse<String> insert(UserAuthDTO userAuthDTO);

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
	ServerResponse<String> update(String id, UserAuthDTO userAuthDTO);

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
	ServerResponse<String> delete(String ids);
}
