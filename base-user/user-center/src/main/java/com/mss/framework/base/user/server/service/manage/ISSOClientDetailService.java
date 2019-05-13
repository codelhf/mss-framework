package com.mss.framework.base.user.server.service.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.SSOClientDetailDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @Title: SSOClientDetailService
 * @Description: SSOClientDetail接口层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
public interface ISSOClientDetailService {

	/**
	 * @Title: list
	 * @Description: 查询SSOClientDetail列表
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
     * @Description: 查询SSOClientDetail对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @return ServerResponse<SSOClientDetailDTO>
     */
	ServerResponse<SSOClientDetailDTO> select(String id);

    /**
     * @Title: insert
     * @Description: 保存SSOClientDetail对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param sSOClientDetailDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> insert(SSOClientDetailDTO sSOClientDetailDTO);

    /**
     * @Title: update
     * @Description: 更新SSOClientDetail对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param id
     * @param sSOClientDetailDTO
     * @return ServerResponse<String>
     */
	ServerResponse<String> update(String id, SSOClientDetailDTO sSOClientDetailDTO);

    /**
     * @Title: delete
     * @Description: 批量删除SSOClientDetail对象
     * @Company: example
     * @Author: liuhf
     * @CreateTime: 2019-05-04 10:00:17
     *
     * @param ids
     * @return ServerResponse<String>
     */
	ServerResponse<String> delete(String ids);
}
