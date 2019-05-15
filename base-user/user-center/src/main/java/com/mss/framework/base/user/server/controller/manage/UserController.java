package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.UserDTO;
import com.mss.framework.base.user.server.service.manage.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: UserController
 * @Description: User控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@RestController
@RequestMapping(value = "/user/manage")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询User列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return userService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询User对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "User主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<UserDTO> select(@PathVariable("id") String id) {
        return userService.select(id);
    }

    @ApiOperation(value = "保存User对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userDTO", value = "UserDTO对象", dataType = "UserDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody UserDTO userDTO) {
        return userService.insert(userDTO);
    }

    @ApiOperation(value = "更新User对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "User主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "userDTO", value = "UserDTO对象", dataType = "UserDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO);
    }

    @ApiOperation(value = "批量删除User对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "User主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return userService.delete(ids);
    }
}
