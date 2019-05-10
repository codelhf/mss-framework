package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.user.server.dto.OAuthAccessTokenDTO;
import com.mss.framework.base.user.server.service.IOAuthAccessTokenService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: OAuthAccessTokenController
 * @Description: OAuthAccessToken控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@RestController
@RequestMapping(value = "/oAuthAccessToken")
public class OAuthAccessTokenController {

    @Autowired
    private IOAuthAccessTokenService iOAuthAccessTokenService;

    @ApiOperation(value = "查询OAuthAccessToken列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return iOAuthAccessTokenService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询OAuthAccessToken对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthAccessToken主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<OAuthAccessTokenDTO> select(@PathVariable("id") String id) {
        return iOAuthAccessTokenService.select(id);
    }

    @ApiOperation(value = "保存OAuthAccessToken对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "oAuthAccessTokenDTO", value = "OAuthAccessTokenDTO对象", dataType = "OAuthAccessTokenDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody OAuthAccessTokenDTO oAuthAccessTokenDTO) {
        return iOAuthAccessTokenService.insert(oAuthAccessTokenDTO);
    }

    @ApiOperation(value = "更新OAuthAccessToken对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthAccessToken主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "oAuthAccessTokenDTO", value = "OAuthAccessTokenDTO对象", dataType = "OAuthAccessTokenDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody OAuthAccessTokenDTO oAuthAccessTokenDTO) {
        return iOAuthAccessTokenService.update(id, oAuthAccessTokenDTO);
    }

    @ApiOperation(value = "批量删除OAuthAccessToken对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "OAuthAccessToken主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return iOAuthAccessTokenService.delete(ids);
    }
}
