package com.mss.framework.base.user.admin.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.dto.SysRoleDTO;
import com.mss.framework.base.user.admin.service.*;
import com.mss.framework.base.user.admin.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRolePowerService sysRolePowerService;
    @Autowired
    private SysRoleUserService sysRoleUserService;

    @GetMapping("/list")
    public ServerResponse list(@RequestParam("pageNum") int pageNum,
                               @RequestParam("pageSize") int pageSize,
                               @RequestParam("roleName") String roleName) {
        return sysRoleService.list(pageNum, pageSize, roleName);
    }

    @PostMapping("/save")
    public ServerResponse save(SysRoleDTO param) {
        return sysRoleService.save(param);
    }

    @PostMapping("/update")
    public ServerResponse update(SysRoleDTO param) {
        return sysRoleService.update(param);
    }

    @GetMapping("/changePowers")
    public ServerResponse changePowers(@RequestParam("roleId") String roleId,
                                       @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        return sysRolePowerService.changeRoleAcls(roleId, aclIdList);
    }

    @GetMapping("/changeUsers")
    public ServerResponse changeUsers(@RequestParam("roleId") String roleId,
                                      @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        return sysRoleUserService.changeRoleUsers(roleId, userIdList);
    }

//    @GetMapping("/roleTree")
//    public ServerResponse roleTree(@RequestParam("roleId") String roleId) {
//        return ServerResponse.createBySuccess(sysTreeService.roleTree(roleId));
//    }

//    @GetMapping("/users")
//    public ServerResponse users(@RequestParam("roleId") int roleId) {
//        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
//        List<SysUser> allUserList = sysUserService.getAll();
//        List<SysUser> unselectedUserList = Lists.newArrayList();
//
//        Set<String> selectedUserSet = selectedUserList.stream().map(SysUser::getId).collect(Collectors.toSet());
//        for (SysUser user : allUserList) {
//            if (user.getStatus() == 1 && !selectedUserSet.contains(user.getId())) {
//                unselectedUserList.add(user);
//            }
//        }
//        //selectedUserList = selectedUserList.stream().filter(sysUser -> sysUser.getStatus() != 1).collect(Collectors.toList());
//        Map<String, List<SysUser>> map = Maps.newHashMap();
//        map.put("selected", selectedUserList);
//        map.put("unselected", unselectedUserList);
//        return ServerResponse.createBySuccess(map);
//    }
}
