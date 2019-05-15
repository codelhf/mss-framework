package com.mss.framework.base.user.server.service.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.OAuthScopeDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: OAuthScopeService
 * @Description: OAuthScope接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
public interface OAuthScopeService {

	/**
	 * @Title: list
	 * @Description: 查询OAuthScope列表
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
     * @Description: 查询OAuthScope对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<OAuthScopeDTO>
     */
	ServerResponse<OAuthScopeDTO> select(String id);

    /**
     * @Title: insert
     * @Description: 保存OAuthScope对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param oAuthScopeDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> insert(OAuthScopeDTO oAuthScopeDTO);

    /**
     * @Title: update
     * @Description: 更新OAuthScope对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param oAuthScopeDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> update(String id, OAuthScopeDTO oAuthScopeDTO);

    /**
     * @Title: delete
     * @Description: 批量删除OAuthScope对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param ids
     * @return ServerResponse<String>
     */
	ServerResponse<String> delete(String ids);
}
