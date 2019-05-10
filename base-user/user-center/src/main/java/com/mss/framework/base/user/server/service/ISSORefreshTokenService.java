package com.mss.framework.base.user.server.service;

import com.mss.framework.base.user.server.dto.SSORefreshTokenDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: SSORefreshTokenService
 * @Description: SSORefreshToken接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
public interface ISSORefreshTokenService {

	/**
	 * @Title: list
	 * @Description: 查询SSORefreshToken列表
	 * @Company: example
	 * @Author: liuhf
	 * @CreateTime: 2019-05-04 10:00:17
	 *
	 * @param pageNum
	 * @param pageSize
	 * @param params
	 * @return ServerResponse<PageInfo>
	 */
	ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Map<String, String> params);

    /**
     * @Title: select
     * @Description: 查询SSORefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @return ServerResponse<SSORefreshTokenDTO>
     */
	ServerResponse<SSORefreshTokenDTO> select(String id);

    /**
     * @Title: insert
     * @Description: 保存SSORefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param sSORefreshTokenDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> insert(SSORefreshTokenDTO sSORefreshTokenDTO);

    /**
     * @Title: update
     * @Description: 更新SSORefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @param sSORefreshTokenDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> update(String id, SSORefreshTokenDTO sSORefreshTokenDTO);

    /**
     * @Title: delete
     * @Description: 批量删除SSORefreshToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param ids
     * @return ServerResponse<String>
     */
	ServerResponse<String> delete(String ids);
}
