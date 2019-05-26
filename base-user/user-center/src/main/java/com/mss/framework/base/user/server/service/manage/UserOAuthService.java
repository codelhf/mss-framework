package com.mss.framework.base.user.server.service.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.UserOAuthDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: UserOAuthService
 * @Description: UserOAuth接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-26 21:51:42
 */
public interface UserOAuthService {

	/**
	 * @Title: list
	 * @Description: 查询UserOAuth列表
	 * @Company: example
	 * @Author: liuhf
	 * @CreateTime: 2019-05-26 21:51:42
	 *
	 * @param pageNum
	 * @param pageSize
	 * @param params
	 * @return ServerResponse<PageInfo>
	 */
	ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Map<String, String> params);

    /**
     * @Title: select
     * @Description: 查询UserOAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-26 21:51:42
     *
     * @param id
     * @return ServerResponse<UserOAuthDTO>
     */
	ServerResponse<UserOAuthDTO> select(String id);

    /**
     * @Title: insert
     * @Description: 保存UserOAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-26 21:51:42
     *
     * @param userOAuthDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> insert(UserOAuthDTO userOAuthDTO);

    /**
     * @Title: update
     * @Description: 更新UserOAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-26 21:51:42
     *
     * @param id
     * @param userOAuthDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> update(String id, UserOAuthDTO userOAuthDTO);

    /**
     * @Title: delete
     * @Description: 批量删除UserOAuth对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-26 21:51:42
     *
     * @param ids
     * @return ServerResponse<String>
     */
	ServerResponse<String> delete(String ids);
}
