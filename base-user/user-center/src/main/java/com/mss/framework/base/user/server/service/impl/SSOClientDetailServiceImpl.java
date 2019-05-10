package com.mss.framework.base.user.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.mss.framework.base.user.server.dao.SSOClientDetailMapper;
import com.mss.framework.base.user.server.dto.SSOClientDetailDTO;
import com.mss.framework.base.user.server.pojo.SSOClientDetail;
import com.mss.framework.base.user.server.service.ISSOClientDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: SSOClientDetailServiceImpl
 * @Description: SSOClientDetail业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@Service
public class SSOClientDetailServiceImpl implements ISSOClientDetailService {

    @Autowired
    private SSOClientDetailMapper sSOClientDetailMapper;

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
    @Override
    public ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Map<String, String> params) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        SSOClientDetail sSOClientDetail = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            sSOClientDetail = JSON.parseObject(JSON.toJSONString(params), SSOClientDetail.class);
        }
        List<SSOClientDetail> sSOClientDetailList = sSOClientDetailMapper.selectPageList(sSOClientDetail);
        List<SSOClientDetailDTO> sSOClientDetailDTOList = new ArrayList<>();
        BeanUtils.copyProperties(sSOClientDetailList, sSOClientDetailDTOList, List.class);
        PageInfo pageInfo = new PageInfo(sSOClientDetailList);
        pageInfo.setList(sSOClientDetailDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
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
    @Override
    public ServerResponse<SSOClientDetailDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        SSOClientDetail sSOClientDetail = sSOClientDetailMapper.selectByPrimaryKey(id);
        if (sSOClientDetail == null) {
            return ServerResponse.createByErrorMessage("SSOClientDetail不存在");
        }
        SSOClientDetailDTO sSOClientDetailDTO = new SSOClientDetailDTO();
        BeanUtils.copyProperties(sSOClientDetail, sSOClientDetailDTO);
        return ServerResponse.createBySuccess(sSOClientDetailDTO);
    }
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
    @Override
    public ServerResponse<String> insert(SSOClientDetailDTO sSOClientDetailDTO) {
        SSOClientDetail sSOClientDetail = new SSOClientDetail();
        BeanUtils.copyProperties(sSOClientDetailDTO, sSOClientDetail);
        int rowCount = sSOClientDetailMapper.insertSelective(sSOClientDetail);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增SSOClientDetail失败");
        }
        return ServerResponse.createBySuccessMessage("新增SSOClientDetail成功");
    }
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
    @Override
    public ServerResponse<String> update(String id, SSOClientDetailDTO sSOClientDetailDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        sSOClientDetailDTO.setId(id);
        SSOClientDetail sSOClientDetail = new SSOClientDetail();
        BeanUtils.copyProperties(sSOClientDetailDTO, sSOClientDetail);
        int rowCount = sSOClientDetailMapper.updateByPrimaryKeySelective(sSOClientDetail);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新SSOClientDetail失败");
        }
        return ServerResponse.createBySuccessMessage("更新SSOClientDetail成功");
    }
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
    @Override
    public ServerResponse<String> delete(String ids) {
        List<String> idList = Splitter.on(",").splitToList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "id不能为空");
        }
        int rowCount = sSOClientDetailMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除SSOClientDetail失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除SSOClientDetail成功");
    }
}
