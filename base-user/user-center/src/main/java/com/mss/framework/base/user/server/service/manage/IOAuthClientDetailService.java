package com.mss.framework.base.user.server.service.manage;

import com.mss.framework.base.user.server.dto.OAuthClientDetailDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: OAuthClientDetailService
 * @Description: OAuthClientDetail接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
public interface IOAuthClientDetailService {

	/**
	 * @Title: list
	 * @Description: 查询OAuthClientDetail列表
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
     * @Description: 查询OAuthClientDetail对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @return ServerResponse<OAuthClientDetailDTO>
     */
	ServerResponse<OAuthClientDetailDTO> select(String id);

    /**
     * @Title: insert
     * @Description: 保存OAuthClientDetail对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param oAuthClientDetailDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> insert(OAuthClientDetailDTO oAuthClientDetailDTO);

    /**
     * @Title: update
     * @Description: 更新OAuthClientDetail对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param id
     * @param oAuthClientDetailDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> update(String id, OAuthClientDetailDTO oAuthClientDetailDTO);

    /**
     * @Title: delete
     * @Description: 批量删除OAuthClientDetail对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-03 19:20:47
     *
     * @param ids
     * @return ServerResponse<String>
     */
	ServerResponse<String> delete(String ids);
}
