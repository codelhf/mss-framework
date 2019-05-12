package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.user.server.dto.SSORefreshTokenDTO;
import com.mss.framework.base.user.server.service.manage.ISSORefreshTokenService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: SSORefreshTokenController
 * @Description: SSORefreshToken控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-04 10:00:17
 */
@RestController
@RequestMapping(value = "/sSORefreshToken")
public class SSORefreshTokenController {

    @Autowired
    private ISSORefreshTokenService iSSORefreshTokenService;

    @ApiOperation(value = "查询SSORefreshToken列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize,
                                           @RequestParam("params") Map<String, String> params) {
        return iSSORefreshTokenService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询SSORefreshToken对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "SSORefreshToken主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<SSORefreshTokenDTO> select(@PathVariable("id") String id) {
        return iSSORefreshTokenService.select(id);
    }

    @ApiOperation(value = "保存SSORefreshToken对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "sSORefreshTokenDTO", value = "SSORefreshTokenDTO对象", dataType = "SSORefreshTokenDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody SSORefreshTokenDTO sSORefreshTokenDTO) {
        return iSSORefreshTokenService.insert(sSORefreshTokenDTO);
    }

    @ApiOperation(value = "更新SSORefreshToken对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "SSORefreshToken主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "sSORefreshTokenDTO", value = "SSORefreshTokenDTO对象", dataType = "SSORefreshTokenDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody SSORefreshTokenDTO sSORefreshTokenDTO) {
        return iSSORefreshTokenService.update(id, sSORefreshTokenDTO);
    }

    @ApiOperation(value = "批量删除SSORefreshToken对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "SSORefreshToken主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return iSSORefreshTokenService.delete(ids);
    }
}
