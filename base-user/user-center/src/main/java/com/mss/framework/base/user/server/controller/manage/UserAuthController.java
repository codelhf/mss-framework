package com.mss.framework.base.user.server.controller.manage;

import com.mss.framework.base.user.server.dto.UserAuthDTO;
import com.mss.framework.base.user.server.service.IUserAuthService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.Map;

/**
 * @Title: UserAuthController
 * @Description: UserAuth控制层
 * @Company: example
 * @Author: liuhf
 * @CreateTime: 2019-05-03 19:20:47
 */
@RestController
@RequestMapping(value = "/userAuth")
public class UserAuthController {

    @Autowired
    private IUserAuthService iUserAuthService;

    @ApiOperation(value = "查询UserAuth列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "分页当前页码", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "pageSize", value = "分页一页大小", dataType = "Integer", required = false),
        @ApiImplicitParam(name = "params", value = "其他查询参数", dataType = "Map", required = false)
    })
    @GetMapping("")
    public ServerResponse<PageInfo> list(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("params") Map<String, String> params) {
        return iUserAuthService.list(pageNum, pageSize, params);
    }

    @ApiOperation(value = "查询UserAuth对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "UserAuth主键", dataType = "String", required = true)
    })
    @GetMapping("/{id}")
    public ServerResponse<UserAuthDTO> select(@PathVariable("id") String id) {
        return iUserAuthService.select(id);
    }

    @ApiOperation(value = "保存UserAuth对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userAuthDTO", value = "UserAuthDTO对象", dataType = "UserAuthDTO", required = true)
    })
    @PostMapping("")
    public ServerResponse<String> insert(@RequestBody UserAuthDTO userAuthDTO) {
        return iUserAuthService.insert(userAuthDTO);
    }

    @ApiOperation(value = "更新UserAuth对象")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "UserAuth主键", dataType = "String", required = true),
        @ApiImplicitParam(name = "userAuthDTO", value = "UserAuthDTO对象", dataType = "UserAuthDTO", required = true)
    })
    @PutMapping("/{id}")
    public ServerResponse<String> update(@PathVariable("id") String id,
                                           @RequestBody UserAuthDTO userAuthDTO) {
        return iUserAuthService.update(id, userAuthDTO);
    }

    @ApiOperation(value = "批量删除UserAuth对象}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "UserAuth主键字符串,用 , 分隔", dataType = "String", required = true)
    })
    @DeleteMapping("/{ids}")
    public ServerResponse<String> delete(@PathVariable("ids") String ids) {
        return iUserAuthService.delete(ids);
    }
}
