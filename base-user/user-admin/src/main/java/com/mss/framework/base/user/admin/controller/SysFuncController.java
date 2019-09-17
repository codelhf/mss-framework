package com.mss.framework.base.user.admin.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dto.SysFuncDTO;
import com.mss.framework.base.user.admin.service.SysFuncService;
import com.mss.framework.base.user.admin.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/func")
@Slf4j
public class SysFuncController {

    @Autowired
    private SysFuncService sysFuncService;
    @Autowired
    private SysTreeService sysTreeService;

    @GetMapping("/tree")
    public ServerResponse tree() {
        List<SysFuncDTO> funcDTOList = sysTreeService.funcTree();
        return ServerResponse.createBySuccess(funcDTOList);
    }

    @GetMapping("/userFuncTree")
    public ServerResponse userFuncTree() {
        String userId = RequestHolder.getCurrentUser().getId();
        List<SysFuncDTO> funcDTOList = sysTreeService.userFuncTree(userId);
        return ServerResponse.createBySuccess(funcDTOList);
    }

    @GetMapping("/list")
    public ServerResponse list(@RequestParam("pageNum") int pageNum,
                         @RequestParam("pageSize") int pageSize,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "parentId", required = false) String parentId){
        return sysFuncService.list(pageNum, pageSize, name, parentId);
    }

    @GetMapping("/select")
    public ServerResponse selectPower(String id) {
        return sysFuncService.select(id);
    }

    @PostMapping("/save")
    public ServerResponse savePower(SysFuncDTO param){
        return sysFuncService.save(param);
    }

    @PostMapping("/update")
    public ServerResponse updatePower(SysFuncDTO param){
        return sysFuncService.update(param);
    }
}
