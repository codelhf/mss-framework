
package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.server.dto.UserLoginDTO;
import com.mss.framework.base.user.server.service.manage.UserLoginService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: UserLoginController
 * @Description: UserLogin控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-15 15:24:33
 */
@RestController
@RequestMapping(value = "/mm/userLogin")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @ApiOperation(value = "查询UserLogin列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return userLoginService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询UserLogin对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "UserLogin主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<UserLoginDTO> select(@PathVariable("id") String id) {
        return userLoginService.select(id);
    }

    @ApiOperation(value = "保存UserLogin对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userLoginDTO", value = "UserLoginDTO对象", dataType = "UserLoginDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody UserLoginDTO userLoginDTO) {
        return userLoginService.insert(userLoginDTO);
    }

    @ApiOperation(value = "更新UserLogin对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "UserLogin主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "userLoginDTO", value = "UserLoginDTO对象", dataType = "UserLoginDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody UserLoginDTO userLoginDTO) {
        return userLoginService.update(id, userLoginDTO);
    }

    @ApiOperation(value = "批量删除UserLogin对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "UserLogin主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return userLoginService.delete(ids);
    }
}
