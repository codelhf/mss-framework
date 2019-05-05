package com.mss.framework.base.user.server.controller;

import com.mss.framework.base.user.server.common.ServerResponse;
import com.mss.framework.base.user.server.dto.SSOAccessTokenDTO;
import com.mss.framework.base.user.server.service.ISSOAccessTokenService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: SSOAccessTokenController
 * @Description: SSOAccessToken控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@RestController
@RequestMapping(value = "/sSOAccessToken")
public class SSOAccessTokenController {

    @Autowired
    private ISSOAccessTokenService iSSOAccessTokenService;

    @ApiOperation(value = "查询SSOAccessToken列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return iSSOAccessTokenService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询SSOAccessToken对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "SSOAccessToken主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<SSOAccessTokenDTO> select(@PathVariable("id") String id) {
        return iSSOAccessTokenService.select(id);
    }

    @ApiOperation(value = "保存SSOAccessToken对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sSOAccessTokenDTO", value = "SSOAccessTokenDTO对象", dataType = "SSOAccessTokenDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody SSOAccessTokenDTO sSOAccessTokenDTO) {
        return iSSOAccessTokenService.insert(sSOAccessTokenDTO);
    }

    @ApiOperation(value = "更新SSOAccessToken对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "SSOAccessToken主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "sSOAccessTokenDTO", value = "SSOAccessTokenDTO对象", dataType = "SSOAccessTokenDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody SSOAccessTokenDTO sSOAccessTokenDTO) {
        return iSSOAccessTokenService.update(id, sSOAccessTokenDTO);
    }

    @ApiOperation(value = "批量删除SSOAccessToken对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "SSOAccessToken主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return iSSOAccessTokenService.delete(ids);
    }
}
