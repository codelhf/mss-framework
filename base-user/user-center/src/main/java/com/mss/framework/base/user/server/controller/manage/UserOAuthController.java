
package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.UserOAuthDTO;
import com.mss.framework.base.user.server.service.manage.UserOAuthService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: UserOAuthController
 * @Description: UserOAuth控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-26 21:51:42
 */
@RestController
@RequestMapping(value = "/mm/userOAuth")
public class UserOAuthController {

    @Autowired
    private UserOAuthService userOAuthService;

    @ApiOperation(value = "查询UserOAuth列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("pageSize") Integer pageSize,
                                            @RequestParam("params") Map<String, String> params) {
        return userOAuthService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询UserOAuth对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "UserOAuth主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<UserOAuthDTO> select(@PathVariable("id") String id) {
        return userOAuthService.select(id);
    }

    @ApiOperation(value = "保存UserOAuth对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userOAuthDTO", value = "UserOAuthDTO对象", dataType = "UserOAuthDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody UserOAuthDTO userOAuthDTO) {
        return userOAuthService.insert(userOAuthDTO);
    }

    @ApiOperation(value = "更新UserOAuth对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "UserOAuth主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "userOAuthDTO", value = "UserOAuthDTO对象", dataType = "UserOAuthDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody UserOAuthDTO userOAuthDTO) {
        return userOAuthService.update(id, userOAuthDTO);
    }

    @ApiOperation(value = "批量删除UserOAuth对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "UserOAuth主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return userOAuthService.delete(ids);
    }
}
