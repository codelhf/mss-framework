package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.user.server.common.ServerResponse;
import com.mss.framework.base.user.server.dto.SSOClientDetailDTO;
import com.mss.framework.base.user.server.service.ISSOClientDetailService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: SSOClientDetailController
 * @Description: SSOClientDetail控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@RestController
@RequestMapping(value = "/sSOClientDetail")
public class SSOClientDetailController {

    @Autowired
    private ISSOClientDetailService iSSOClientDetailService;

    @ApiOperation(value = "查询SSOClientDetail列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return iSSOClientDetailService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询SSOClientDetail对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "SSOClientDetail主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<SSOClientDetailDTO> select(@PathVariable("id") String id) {
        return iSSOClientDetailService.select(id);
    }

    @ApiOperation(value = "保存SSOClientDetail对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sSOClientDetailDTO", value = "SSOClientDetailDTO对象", dataType = "SSOClientDetailDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody SSOClientDetailDTO sSOClientDetailDTO) {
        return iSSOClientDetailService.insert(sSOClientDetailDTO);
    }

    @ApiOperation(value = "更新SSOClientDetail对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "SSOClientDetail主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "sSOClientDetailDTO", value = "SSOClientDetailDTO对象", dataType = "SSOClientDetailDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody SSOClientDetailDTO sSOClientDetailDTO) {
        return iSSOClientDetailService.update(id, sSOClientDetailDTO);
    }

    @ApiOperation(value = "批量删除SSOClientDetail对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "SSOClientDetail主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return iSSOClientDetailService.delete(ids);
    }
}
