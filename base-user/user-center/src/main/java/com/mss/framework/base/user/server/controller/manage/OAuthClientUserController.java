package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.OAuthClientUserDTO;
import com.mss.framework.base.user.server.service.manage.OAuthClientUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: OAuthClientUserController
 * @Description: OAuthClientUser控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@RestController
@RequestMapping(value = "/oAuthClientUser")
public class OAuthClientUserController {

    @Autowired
    private OAuthClientUserService oAuthClientUserService;

    @ApiOperation(value = "查询OAuthClientUser列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return oAuthClientUserService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询OAuthClientUser对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthClientUser主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<OAuthClientUserDTO> select(@PathVariable("id") String id) {
        return oAuthClientUserService.select(id);
    }

    @ApiOperation(value = "保存OAuthClientUser对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "oAuthClientUserDTO", value = "OAuthClientUserDTO对象", dataType = "OAuthClientUserDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody OAuthClientUserDTO oAuthClientUserDTO) {
        return oAuthClientUserService.insert(oAuthClientUserDTO);
    }

    @ApiOperation(value = "更新OAuthClientUser对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthClientUser主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "oAuthClientUserDTO", value = "OAuthClientUserDTO对象", dataType = "OAuthClientUserDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody OAuthClientUserDTO oAuthClientUserDTO) {
        return oAuthClientUserService.update(id, oAuthClientUserDTO);
    }

    @ApiOperation(value = "批量删除OAuthClientUser对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "OAuthClientUser主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return oAuthClientUserService.delete(ids);
    }
}
