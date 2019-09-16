package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.SysPowerMapper;
import com.mss.framework.base.user.admin.dto.SysPowerDTO;
import com.mss.framework.base.user.admin.pojo.SysPower;
import com.mss.framework.base.user.admin.util.BeanValidator;
import com.mss.framework.base.user.admin.util.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SysPowerService {

    @Autowired
    private SysPowerMapper sysPowerMapper;
    @Autowired
    private SysLogService sysLogService;

    public ServerResponse list(int pageNum, int pageSize, String name, String parentId){
        IPage<SysPower> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysPower> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like("name", name);
        }
        if (StringUtils.isNotBlank(parentId)) {
            wrapper.eq("parent_id", parentId);
        }
        wrapper.orderByDesc("update_time");
        IPage<SysPower> sysPowerIPage = sysPowerMapper.selectPage(page, wrapper);
        return ServerResponse.createBySuccess(sysPowerIPage);
    }

    public ServerResponse select(String id) {
        SysPower sysPower = sysPowerMapper.selectById(id);
        if (sysPower == null) {
            return ServerResponse.createByErrorMessage("没有查询到该功能信息");
        }
        return ServerResponse.createBySuccess(sysPower);
    }

    public ServerResponse save(SysPowerDTO param){
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName())){
            return ServerResponse.createByErrorMessage("当前功能模块下存在相同的功能点名称");
        }
        SysPower power = SysPower.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .url(param.getUrl())
                .type(param.getType())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        power.setCode(generateCode());
        power.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        power.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        power.setUpdateTime(new Date());
        int rowCount = sysPowerMapper.insert(power);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增功能失败");
        }
        sysLogService.savePowerLog(null, power);
        return ServerResponse.createBySuccessMessage("新增功能正常");
    }

    private boolean checkExist(String parentId, String name){
        QueryWrapper<SysPower> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        wrapper.eq("name", name);
        return sysPowerMapper.selectCount(wrapper) > 0;
    }

    private String generateCode(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }

    public ServerResponse update(SysPowerDTO param){
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName())){
            return ServerResponse.createByErrorMessage("当前功能模块下存在相同的功能点名称");
        }
        SysPower before = sysPowerMapper.selectById(param.getId());
        Preconditions.checkNotNull(before, "待更新的功能点不存在");

        SysPower after = SysPower.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .url(param.getUrl())
                .type(param.getType())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        after.setCode(generateCode());
        after.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        after.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setUpdateTime(new Date());
        int rowCount = sysPowerMapper.updateById(after);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新功能失败");
        }
        sysLogService.savePowerLog(before, after);
        return ServerResponse.createBySuccessMessage("更新功能正常");
    }
}
