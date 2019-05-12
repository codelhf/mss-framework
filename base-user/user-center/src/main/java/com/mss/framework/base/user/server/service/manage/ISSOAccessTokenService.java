package com.mss.framework.base.user.server.service.manage;

import com.mss.framework.base.user.server.dto.SSOAccessTokenDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: SSOAccessTokenService
 * @Description: SSOAccessToken接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
public interface ISSOAccessTokenService {

	/**
	 * @Title: list
	 * @Description: 查询SSOAccessToken列表
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
     * @Description: 查询SSOAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @return ServerResponse<SSOAccessTokenDTO>
     */
	ServerResponse<SSOAccessTokenDTO> select(String id);

    /**
     * @Title: insert
     * @Description: 保存SSOAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param sSOAccessTokenDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> insert(SSOAccessTokenDTO sSOAccessTokenDTO);

    /**
     * @Title: update
     * @Description: 更新SSOAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @param sSOAccessTokenDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> update(String id, SSOAccessTokenDTO sSOAccessTokenDTO);

    /**
     * @Title: delete
     * @Description: 批量删除SSOAccessToken对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param ids
     * @return ServerResponse<String>
     */
	ServerResponse<String> delete(String ids);
}
