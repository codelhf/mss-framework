package com.mss.framework.base.user.admin.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.dto.SysUserDTO;
import com.mss.framework.base.user.admin.pojo.SysUser;
import com.mss.framework.base.user.admin.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public ServerResponse list(@RequestParam("pageNum") int pageNum,
                               @RequestParam("pageSize") int pageSize,
                               @RequestParam(value = "username", required = false) String username,
                               @RequestParam(value = "phone", required = false) String phone,
                               @RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "deptId", required = false) String deptId) {
        return sysUserService.list(pageNum, pageSize, username, phone, email, deptId);
    }

    @GetMapping("/select")
    public ServerResponse<SysUser> select(@RequestParam("id") String id) {
        return null;
    }

    @PostMapping("/save")
    public ServerResponse<String> save(SysUserDTO param) {
        return sysUserService.save(param);
    }

    @PostMapping("/update")
    public ServerResponse<String> update(SysUserDTO param) {
        return sysUserService.update(param);
    }
}
