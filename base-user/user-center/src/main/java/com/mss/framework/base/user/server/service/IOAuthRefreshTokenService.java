package com.mss.framework.base.user.server.service;

import com.mss.framework.base.user.server.common.ServerResponse;
import com.mss.framework.base.user.server.dto.OAuthRefreshTokenDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: OAuthRefreshTokenService
 * @Description: OAuthRefreshToken接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
public interface IOAuthRefreshTokenService {

	/**
	 * @Title: list
	 * @Description: 查询OAuthRefreshToken列表
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
     * @Description: 查询OAuthRefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<OAuthRefreshTokenDTO>
     */
	ServerResponse<OAuthRefreshTokenDTO> select(String id);

    /**
     * @Title: insert
     * @Description: 保存OAuthRefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param oAuthRefreshTokenDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> insert(OAuthRefreshTokenDTO oAuthRefreshTokenDTO);

    /**
     * @Title: update
     * @Description: 更新OAuthRefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param oAuthRefreshTokenDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> update(String id, OAuthRefreshTokenDTO oAuthRefreshTokenDTO);

    /**
     * @Title: delete
     * @Description: 批量删除OAuthRefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param ids
     * @return ServerResponse<String>
     */
	ServerResponse<String> delete(String ids);
}
