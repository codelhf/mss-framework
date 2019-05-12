package com.mss.framework.base.user.server.service.manage;

import com.mss.framework.base.user.server.dto.OAuthAccessTokenDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: OAuthAccessTokenService
 * @Description: OAuthAccessToken接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
public interface IOAuthAccessTokenService {

	/**
	 * @Title: list
	 * @Description: 查询OAuthAccessToken列表
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
     * @Description: 查询OAuthAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<OAuthAccessTokenDTO>
     */
	ServerResponse<OAuthAccessTokenDTO> select(String id);

    /**
     * @Title: insert
     * @Description: 保存OAuthAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param oAuthAccessTokenDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> insert(OAuthAccessTokenDTO oAuthAccessTokenDTO);

    /**
     * @Title: update
     * @Description: 更新OAuthAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param oAuthAccessTokenDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> update(String id, OAuthAccessTokenDTO oAuthAccessTokenDTO);

    /**
     * @Title: delete
     * @Description: 批量删除OAuthAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param ids
     * @return ServerResponse<String>
     */
	ServerResponse<String> delete(String ids);
}
