package com.mss.framework.base.user.admin.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.dto.SysDeptDTO;
import com.mss.framework.base.user.admin.service.SysTreeService;
import com.mss.framework.base.user.admin.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysTreeService sysTreeService;

    @GetMapping("/tree")
    public ServerResponse tree(){
        List<SysDeptDTO> dtoList = sysTreeService.deptTree();
        return ServerResponse.createBySuccess(dtoList);
    }

    @GetMapping("/list")
    public ServerResponse deptList(@RequestParam("pageNum") int pageNum,
                                   @RequestParam("pageSize") int pageSize,
                                   @RequestParam(value = "deptName", required = false) String deptName,
                                   @RequestParam(value = "parentId", required = false) String parentId) {
        return sysDeptService.list(pageNum, pageSize, deptName, parentId);
    }

    @GetMapping("/select")
    public ServerResponse select(String id) {
        return sysDeptService.select(id);
    }

    @PostMapping("/save")
    public ServerResponse saveDept(SysDeptDTO param){
        return sysDeptService.save(param);
    }

    @PostMapping("/update")
    public ServerResponse updateDept(SysDeptDTO param){
        return sysDeptService.update(param);
    }

    @DeleteMapping("/delete")
    public ServerResponse deleteDept(@RequestParam("id") String id){
        return sysDeptService.delete(id);
    }
}
