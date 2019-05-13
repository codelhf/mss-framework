package com.mss.framework.base.user.server.service.manage.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.mss.framework.base.core.common.ResponseCode;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dao.OAuthClientDetailMapper;
import com.mss.framework.base.user.server.dto.OAuthClientDetailDTO;
import com.mss.framework.base.user.server.pojo.OAuthClientDetail;
import com.mss.framework.base.user.server.service.manage.IOAuthClientDetailService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: OAuthClientDetailServiceImpl
 * @Description: OAuthClientDetail业务层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@Service
public class OAuthClientDetailServiceImpl implements IOAuthClientDetailService {

    @Autowired
    private OAuthClientDetailMapper oAuthClientDetailMapper;

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
    @Override
    public ServerResponse<PageInfo> list(Integer pageNum, Integer pageSize, Map<String, String> params) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        OAuthClientDetail oAuthClientDetail = null;
        if (CollectionUtils.isNotEmpty(params.values())) {
            oAuthClientDetail = JSON.parseObject(JSON.toJSONString(params), OAuthClientDetail.class);
        }
        List<OAuthClientDetail> oAuthClientDetailList = oAuthClientDetailMapper.selectPageList(oAuthClientDetail);
        List<OAuthClientDetailDTO> oAuthClientDetailDTOList = new ArrayList<>();
        BeanUtils.copyProperties(oAuthClientDetailList, oAuthClientDetailDTOList, List.class);
        PageInfo pageInfo = new PageInfo(oAuthClientDetailList);
        pageInfo.setList(oAuthClientDetailDTOList);
        return ServerResponse.createBySuccess(pageInfo);
    }
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
    @Override
    public ServerResponse<OAuthClientDetailDTO> select(String id) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        OAuthClientDetail oAuthClientDetail = oAuthClientDetailMapper.selectByPrimaryKey(id);
        if (oAuthClientDetail == null) {
            return ServerResponse.createByErrorMessage("OAuthClientDetail不存在");
        }
        OAuthClientDetailDTO oAuthClientDetailDTO = new OAuthClientDetailDTO();
        BeanUtils.copyProperties(oAuthClientDetail, oAuthClientDetailDTO);
        return ServerResponse.createBySuccess(oAuthClientDetailDTO);
    }
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
    @Override
    public ServerResponse<String> insert(OAuthClientDetailDTO oAuthClientDetailDTO) {
        OAuthClientDetail oAuthClientDetail = new OAuthClientDetail();
        BeanUtils.copyProperties(oAuthClientDetailDTO, oAuthClientDetail);
        int rowCount = oAuthClientDetailMapper.insertSelective(oAuthClientDetail);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增OAuthClientDetail失败");
        }
        return ServerResponse.createBySuccessMessage("新增OAuthClientDetail成功");
    }
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
    @Override
    public ServerResponse<String> update(String id, OAuthClientDetailDTO oAuthClientDetailDTO) {
        if (StringUtils.isBlank(String.valueOf(id))) {
            return ServerResponse.createByErrorMessage("id不能为空");
        }
        oAuthClientDetailDTO.setId(id);
        OAuthClientDetail oAuthClientDetail = new OAuthClientDetail();
        BeanUtils.copyProperties(oAuthClientDetailDTO, oAuthClientDetail);
        int rowCount = oAuthClientDetailMapper.updateByPrimaryKeySelective(oAuthClientDetail);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新OAuthClientDetail失败");
        }
        return ServerResponse.createBySuccessMessage("更新OAuthClientDetail成功");
    }
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
    @Override
    public ServerResponse<String> delete(String ids) {
        List<String> idList = Splitter.on(",").splitToList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), "id不能为空");
        }
        int rowCount = oAuthClientDetailMapper.deleteByIdList(idList);
        if (rowCount == 0 || rowCount < idList.size()) {
            return ServerResponse.createByErrorMessage("批量删除OAuthClientDetail失败");
        }
        return ServerResponse.createBySuccessMessage("批量删除OAuthClientDetail成功");
    }
}
