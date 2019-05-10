package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.user.server.dto.OAuthScopeDTO;
import com.mss.framework.base.user.server.service.IOAuthScopeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: OAuthScopeController
 * @Description: OAuthScope控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@RestController
@RequestMapping(value = "/oAuthScope")
public class OAuthScopeController {

    @Autowired
    private IOAuthScopeService iOAuthScopeService;

    @ApiOperation(value = "查询OAuthScope列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return iOAuthScopeService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询OAuthScope对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthScope主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<OAuthScopeDTO> select(@PathVariable("id") String id) {
        return iOAuthScopeService.select(id);
    }

    @ApiOperation(value = "保存OAuthScope对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "oAuthScopeDTO", value = "OAuthScopeDTO对象", dataType = "OAuthScopeDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody OAuthScopeDTO oAuthScopeDTO) {
        return iOAuthScopeService.insert(oAuthScopeDTO);
    }

    @ApiOperation(value = "更新OAuthScope对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthScope主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "oAuthScopeDTO", value = "OAuthScopeDTO对象", dataType = "OAuthScopeDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody OAuthScopeDTO oAuthScopeDTO) {
        return iOAuthScopeService.update(id, oAuthScopeDTO);
    }

    @ApiOperation(value = "批量删除OAuthScope对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "OAuthScope主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return iOAuthScopeService.delete(ids);
    }
}
