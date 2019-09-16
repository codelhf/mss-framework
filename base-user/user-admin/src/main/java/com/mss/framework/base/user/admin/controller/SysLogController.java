package com.mss.framework.base.user.admin.controller;

import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.dto.SearchLogDto;
import com.mss.framework.base.user.admin.service.SysLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @GetMapping("/list")
    public ServerResponse list(@RequestParam("pageNum") int pageNum,
                               @RequestParam("pageSize") int pageSize,
                               SearchLogDto params){
        return sysLogService.searchPageList(pageNum, pageSize, params);
    }

    @GetMapping("/recover/{id}")
    public ServerResponse recover(@PathVariable("id") String id){
        sysLogService.recover(id);
        return ServerResponse.createBySuccess();
    }
}
