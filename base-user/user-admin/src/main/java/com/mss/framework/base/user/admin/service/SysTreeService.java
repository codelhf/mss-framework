package com.mss.framework.base.user.admin.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mss.framework.base.user.admin.common.Constants;
import com.mss.framework.base.user.admin.dao.SysDeptMapper;
import com.mss.framework.base.user.admin.dao.SysFuncMapper;
import com.mss.framework.base.user.admin.dto.SysDeptDTO;
import com.mss.framework.base.user.admin.dto.SysFuncDTO;
import com.mss.framework.base.user.admin.pojo.*;
import com.mss.framework.base.user.admin.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysTreeService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysFuncMapper sysFuncMapper;
    @Autowired
    private SysCoreService sysCoreService;


    public List<SysDeptDTO> deptTree(){
        List<SysDept> groupList = sysDeptMapper.selectList(null);
        List<SysDeptDTO> dtoList = Lists.newArrayList();
        for (SysDept group : groupList) {
            SysDeptDTO groupDTO = SysDeptDTO.adapter(group);
            dtoList.add(groupDTO);
        }
        return deptListToTree(dtoList);
    }

    private List<SysDeptDTO> deptListToTree(List<SysDeptDTO> deptLevelList){
        if (CollectionUtils.isEmpty(deptLevelList)){
            return Lists.newArrayList();
        }
        // level -> [dept1, dept2, ...]
        Multimap<String, SysDeptDTO> deptLevelMap = ArrayListMultimap.create();
        List<SysDeptDTO> rootList = Lists.newArrayList();

        for (SysDeptDTO dto: deptLevelList) {
            deptLevelMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 按照seq从小到大排序
        rootList.sort(deptSeqComparable);
        // 递归生成树
        transformDeptTree(rootList, LevelUtil.ROOT, deptLevelMap);
        return rootList;
    }
    // all 0->0.1,0.2
    // level:0
    // level:0.1
    // level:0.2
    private void transformDeptTree(List<SysDeptDTO> deptLevelList, String level, Multimap<String, SysDeptDTO> deptLevelMap) {
        for (SysDeptDTO deptLevelDto : deptLevelList) {
            // 遍历该层的每个元素
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getSeq());
            // 处理下一层
            List<SysDeptDTO> tempDeptList = (List<SysDeptDTO>) deptLevelMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                tempDeptList.sort(deptSeqComparable);
                // 设置下一层部门
                deptLevelDto.setChildren(tempDeptList);
                // 进入下一层处理
                transformDeptTree(tempDeptList, nextLevel, deptLevelMap);
            }
        }
    }


    public List<SysFuncDTO> userFuncTree(String userId){
        List<SysFunc> userFuncList = sysCoreService.getUserFuncList(userId);
        List<SysFuncDTO> funcDTOList = Lists.newArrayList();
        for (SysFunc sysFunc : userFuncList){
            SysFuncDTO dto = SysFuncDTO.adapter(sysFunc);
            dto.setHasAcl(true);
            dto.setChecked(true);
            funcDTOList.add(dto);
        }
        return funcListToTree(funcDTOList);
    }

    public List<SysFuncDTO> roleFuncTree(String roleId){
        // 1. 当前用户已分配的权限点
        List<SysFunc> userFuncList = sysCoreService.getCurrentUserFuncList();
        // 2. 当前角色已分配的权限点
        List<SysFunc> roleFuncList = sysCoreService.getRoleFuncList(roleId);
        // 3. 当前系统所有的权限点
        List<SysFuncDTO> funcList = Lists.newArrayList();

        Set<String> userFuncIdSet = userFuncList.stream().map(SysFunc::getId).collect(Collectors.toSet());
        Set<String> roleFuncIdSet = roleFuncList.stream().map(SysFunc::getId).collect(Collectors.toSet());

        List<SysFunc> allFuncList = sysFuncMapper.selectList(null);
        for (SysFunc func : allFuncList){
            SysFuncDTO dto = SysFuncDTO.adapter(func);
            if (userFuncIdSet.contains(func.getId())) {
                dto.setHasAcl(true);
            }
            if (roleFuncIdSet.contains(func.getId())) {
                dto.setChecked(true);
            }
            funcList.add(dto);
        }
        return funcListToTree(funcList);
    }

    public List<SysFuncDTO> funcTree(){
        List<SysFunc> allFuncList = sysFuncMapper.selectList(null);
        List<SysFuncDTO> funcDTOList = Lists.newArrayList();
        for (SysFunc sysFunc : allFuncList){
            SysFuncDTO dto = SysFuncDTO.adapter(sysFunc);
            funcDTOList.add(dto);
        }
        return funcListToTree(funcDTOList);
    }

    private List<SysFuncDTO> funcListToTree(List<SysFuncDTO> funcDTOList){
        if (CollectionUtils.isEmpty(funcDTOList)){
            return Lists.newArrayList();
        }

        Multimap<String, SysFuncDTO> funcLevelMap = ArrayListMultimap.create();
        List<SysFuncDTO> rootList = Lists.newArrayList();

        for (SysFuncDTO dto : funcDTOList){
            if (dto.getStatus() != Constants.StatusEnum.NORMAL.getCode()) {
                continue;
            }
            funcLevelMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }
        // 按照seq从小到大排序
        rootList.sort(funcSeqComparable);
        // 递归生成树
        transformFuncTree(rootList, LevelUtil.ROOT, funcLevelMap);
        return rootList;
    }

    private void transformFuncTree(List<SysFuncDTO> funcLevelList, String level, Multimap<String, SysFuncDTO> funcLevelMap) {
        for (SysFuncDTO funcLevelDTO: funcLevelList) {
            // 遍历该层的每个元素
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, funcLevelDTO.getSeq());
            // 处理下一层
            List<SysFuncDTO> tempFuncList = (List<SysFuncDTO>) funcLevelMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempFuncList)) {
                // 排序
                tempFuncList.sort(funcSeqComparable);
                // 设置下一层部门
                funcLevelDTO.setChildren(tempFuncList);
                // 进入下一层处理
                transformFuncTree(tempFuncList, nextLevel, funcLevelMap);
            }
        }
    }

    private Comparator<SysDeptDTO> deptSeqComparable = new Comparator<SysDeptDTO>() {
        @Override
        public int compare(SysDeptDTO o1, SysDeptDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    private Comparator<SysFuncDTO> funcSeqComparable = new Comparator<SysFuncDTO>() {
        @Override
        public int compare(SysFuncDTO o1, SysFuncDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
