package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.OAuthRefreshTokenDTO;
import com.mss.framework.base.user.server.service.manage.IOAuthRefreshTokenService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: OAuthRefreshTokenController
 * @Description: OAuthRefreshToken控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@RestController
@RequestMapping(value = "/oAuthRefreshToken")
public class OAuthRefreshTokenController {

    @Autowired
    private IOAuthRefreshTokenService iOAuthRefreshTokenService;

    @ApiOperation(value = "查询OAuthRefreshToken列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return iOAuthRefreshTokenService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询OAuthRefreshToken对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthRefreshToken主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<OAuthRefreshTokenDTO> select(@PathVariable("id") String id) {
        return iOAuthRefreshTokenService.select(id);
    }

    @ApiOperation(value = "保存OAuthRefreshToken对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "oAuthRefreshTokenDTO", value = "OAuthRefreshTokenDTO对象", dataType = "OAuthRefreshTokenDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody OAuthRefreshTokenDTO oAuthRefreshTokenDTO) {
        return iOAuthRefreshTokenService.insert(oAuthRefreshTokenDTO);
    }

    @ApiOperation(value = "更新OAuthRefreshToken对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthRefreshToken主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "oAuthRefreshTokenDTO", value = "OAuthRefreshTokenDTO对象", dataType = "OAuthRefreshTokenDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody OAuthRefreshTokenDTO oAuthRefreshTokenDTO) {
        return iOAuthRefreshTokenService.update(id, oAuthRefreshTokenDTO);
    }

    @ApiOperation(value = "批量删除OAuthRefreshToken对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "OAuthRefreshToken主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return iOAuthRefreshTokenService.delete(ids);
    }
}
