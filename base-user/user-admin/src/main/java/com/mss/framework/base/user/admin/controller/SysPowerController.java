package com.mss.framework.base.user.admin.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.dto.SysPowerDTO;
import com.mss.framework.base.user.admin.service.SysPowerService;
import com.mss.framework.base.user.admin.service.SysRoleService;
import com.mss.framework.base.user.admin.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/power")
@Slf4j
public class SysPowerController {

    @Autowired
    private SysPowerService sysPowerService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysTreeService sysTreeService;

    @GetMapping("/tree")
    public ServerResponse tree() {
        return null;
    }

    @GetMapping("/list")
    public ServerResponse list(@RequestParam("pageNum") int pageNum,
                         @RequestParam("pageSize") int pageSize,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "parentId", required = false) String parentId){
        return sysPowerService.list(pageNum, pageSize, name, parentId);
    }

    @GetMapping("/select")
    public ServerResponse selectPower(String id) {
        return sysPowerService.select(id);
    }

    @PostMapping("/save")
    public ServerResponse savePower(SysPowerDTO param){
        return sysPowerService.save(param);
    }

    @PostMapping("/update")
    public ServerResponse updatePower(SysPowerDTO param){
        return sysPowerService.update(param);
    }

//    @GetMapping("/acls")
//    public JsonData acls(@RequestParam("powerId") String powerId){
//        Map<String, Object> map = Maps.newHashMap();
//        List<SysRole> roleList = sysRoleService.getRoleListByAclId(powerId);
//        map.put("roles", roleList);
//        map.put("users", sysRoleService.getUserListByRoleList(roleList));
//        return JsonData.success(map);
//    }
}
