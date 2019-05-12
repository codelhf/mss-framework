package com.mss.framework.base.user.server.service.manage;

import com.mss.framework.base.user.server.dto.OAuthClientUserDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: OAuthClientUserService
 * @Description: OAuthClientUser接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
public interface IOAuthClientUserService {

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
	ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Map<String, String> params);

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
	ServerResponse<OAuthClientUserDTO> select(String id);

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
	ServerResponse<String> insert(OAuthClientUserDTO oAuthClientUserDTO);

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
	ServerResponse<String> update(String id, OAuthClientUserDTO oAuthClientUserDTO);

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
	ServerResponse<String> delete(String ids);
}
