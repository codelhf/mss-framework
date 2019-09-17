package com.mss.framework.base.user.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.mss.framework.base.core.common.ServerResponse;
import com.mss.framework.base.core.util.IDUtil;
import com.mss.framework.base.user.admin.common.RequestHolder;
import com.mss.framework.base.user.admin.dao.SysDeptMapper;
import com.mss.framework.base.user.admin.dao.SysUserMapper;
import com.mss.framework.base.user.admin.dto.SysDeptDTO;
import com.mss.framework.base.user.admin.excepton.ParamException;
import com.mss.framework.base.user.admin.pojo.SysDept;
import com.mss.framework.base.user.admin.pojo.SysUser;
import com.mss.framework.base.user.admin.util.BeanValidator;
import com.mss.framework.base.user.admin.util.IpUtil;
import com.mss.framework.base.user.admin.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    public ServerResponse list(int pageNum, int pageSize, String deptName, String parentId) {
        IPage<SysDept> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(deptName)) {
            wrapper.like("name", deptName);
        }
        if (StringUtils.isNotBlank(parentId)) {
            wrapper.eq("parent_id", parentId);
        }
        wrapper.orderByDesc("update_time");
        IPage<SysDept> deptList = sysDeptMapper.selectPage(page, wrapper);
        return ServerResponse.createBySuccess(deptList);
    }

    public ServerResponse select(String id) {
        SysDept sysDept = sysDeptMapper.selectById(id);
        if (sysDept == null) {
            return ServerResponse.createByErrorMessage("部门信息不存在");
        }
        return ServerResponse.createBySuccess(sysDept);
    }

    public ServerResponse save(SysDeptDTO param){
        BeanValidator.check(param);
        if (checkExits(param.getParentId(), param.getName(), param.getId())){
            throw new ParamException("同一部门下存在相同名称的部门");
        }
        SysDept dept = SysDept.builder()
                .id(IDUtil.UUIDStr())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getSeq()));

        dept.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        dept.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setUpdateTime(new Date());
        int rowCount = sysDeptMapper.insert(dept);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("新增部门失败");
        }
        sysLogService.saveDeptLog(null, dept);
        return ServerResponse.createBySuccessMessage("新增部门成功");
    }

    public ServerResponse update(SysDeptDTO param){
        BeanValidator.check(param);
        if (checkExits(param.getParentId(), param.getName(), param.getId())){
            throw new ParamException("同一部门下存在相同名称的部门");
        }
        SysDept before = sysDeptMapper.selectById(param.getId());
        Preconditions.checkNotNull(before, "更新部门不存在");
        if (checkExits(param.getParentId(), param.getName(), param.getId())){
            throw new ParamException("同一部门下存在相同名称的部门");
        }
        SysDept after = SysDept.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getSeq()));

        after.setUpdateUser(RequestHolder.getCurrentUser().getUsername());
        after.setUpdateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setUpdateTime(new Date());
        updateChild(before, after);
        int rowCount = sysDeptMapper.updateById(after);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("更新部门失败");
        }
        sysLogService.saveDeptLog(before, after);
        return ServerResponse.createBySuccessMessage("更新部门成功");
    }

    @Transactional
    void updateChild(SysDept before, SysDept after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())){
            QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
            wrapper.like("level", before.getLevel() == null ? ".%" : before.getLevel());
            List<SysDept> deptList = sysDeptMapper.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(deptList)) {
                for(SysDept dept: deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0){
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateById(after);
    }

    private boolean checkExits(String parentId, String deptName, String deptId){
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        wrapper.eq("name", deptName);
        wrapper.eq("id", deptId);
        return sysDeptMapper.selectCount(wrapper) > 0;
    }

    private String getLevel(String deptId){
        SysDept dept = sysDeptMapper.selectById(deptId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();
    }

    public ServerResponse delete(String deptId){
        SysDept dept = sysDeptMapper.selectById(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
        QueryWrapper<SysDept> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", deptId);
        if (sysDeptMapper.selectCount(wrapper) > 0){
            return ServerResponse.createByErrorMessage("当前部门下面有子部门，无法删除");
        }
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        if (sysUserMapper.selectCount(queryWrapper) > 0){
            return ServerResponse.createByErrorMessage("当前部门下面有用户，无法删除");
        }
        int rowCount = sysDeptMapper.deleteById(deptId);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("删除部门失败");
        }
        return ServerResponse.createBySuccessMessage("删除部门成功");
    }
}
