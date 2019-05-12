package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.user.server.dto.OAuthClientDetailDTO;
import com.mss.framework.base.user.server.service.manage.IOAuthClientDetailService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: OAuthClientDetailController
 * @Description: OAuthClientDetail控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@RestController
@RequestMapping(value = "/oAuthClientDetail")
public class OAuthClientDetailController {

    @Autowired
    private IOAuthClientDetailService iOAuthClientDetailService;

    @ApiOperation(value = "查询OAuthClientDetail列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return iOAuthClientDetailService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询OAuthClientDetail对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthClientDetail主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<OAuthClientDetailDTO> select(@PathVariable("id") String id) {
        return iOAuthClientDetailService.select(id);
    }

    @ApiOperation(value = "保存OAuthClientDetail对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "oAuthClientDetailDTO", value = "OAuthClientDetailDTO对象", dataType = "OAuthClientDetailDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody OAuthClientDetailDTO oAuthClientDetailDTO) {
        return iOAuthClientDetailService.insert(oAuthClientDetailDTO);
    }

    @ApiOperation(value = "更新OAuthClientDetail对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "OAuthClientDetail主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "oAuthClientDetailDTO", value = "OAuthClientDetailDTO对象", dataType = "OAuthClientDetailDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody OAuthClientDetailDTO oAuthClientDetailDTO) {
        return iOAuthClientDetailService.update(id, oAuthClientDetailDTO);
    }

    @ApiOperation(value = "批量删除OAuthClientDetail对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "OAuthClientDetail主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return iOAuthClientDetailService.delete(ids);
    }
}
