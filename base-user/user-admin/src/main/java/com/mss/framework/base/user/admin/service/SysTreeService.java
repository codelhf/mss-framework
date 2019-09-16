package com.mss.framework.base.user.admin.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mss.framework.base.user.admin.dao.SysDeptMapper;
import com.mss.framework.base.user.admin.dao.SysPowerMapper;
import com.mss.framework.base.user.admin.dto.AclDto;
import com.mss.framework.base.user.admin.dto.AclModuleLevelDto;
import com.mss.framework.base.user.admin.dto.SysDeptDTO;
import com.mss.framework.base.user.admin.pojo.*;
import com.mss.framework.base.user.admin.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysTreeService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysPowerMapper sysPowerMapper;
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
        Multimap<String, SysDeptDTO> levelDeptMap = ArrayListMultimap.create();
        List<SysDeptDTO> rootList = Lists.newArrayList();

        for (SysDeptDTO dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 按照seq从小到大排序
        Collections.sort(rootList, deptSeqComparable);
        // 递归生成树
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }
    // level:0, 0, all 0->0.1,0.2
    // level:0.1
    // level:0.2
    public void transformDeptTree(List<SysDeptDTO> deptLevelList, String level, Multimap<String, SysDeptDTO> levelDeptMap) {
        for(int i = 0; i < deptLevelList.size(); i++){
            // 遍历该层的每个元素
            SysDeptDTO deptLevelDto = deptLevelList.get(i);
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 处理下一层
            List<SysDeptDTO> tempDeptList = (List<SysDeptDTO>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)){
                // 排序
                Collections.sort(tempDeptList, deptSeqComparable);
                // 设置下一层部门
                deptLevelDto.setChildren(tempDeptList);
                // 进入下一层处理
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }


    public List<AclModuleLevelDto> userAclTree(String userId){
        List<SysPower> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysPower acl : userAclList){
            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    public List<AclModuleLevelDto> roleTree(String roleId){
        // 1. 当前用已分配的权限点
        List<SysPower> userAclList = sysCoreService.getCurrentUserAclList();
        // 2. 当前角色分配的权限点
        List<SysPower> roleAclList = sysCoreService.getRoleAclList(roleId);
        // 3. 当前系统所有的权限点
        List<AclDto> aclDtoList = Lists.newArrayList();

        Set<String> userAclIdSet = userAclList.stream().map(SysPower::getId).collect(Collectors.toSet());
        Set<String> roleAclIdSet = roleAclList.stream().map(SysPower::getId).collect(Collectors.toSet());

        List<SysPower> allAclList = sysPowerMapper.selectList(null);
        for (SysPower acl : allAclList){
            AclDto dto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())) {
                dto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())) {
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList){
        if (CollectionUtils.isEmpty(aclDtoList)){
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto acl : aclDtoList){
            if (acl.getStatus() == 1){
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }
        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }


    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList, Multimap<Integer, AclDto> moduleIdAclMap){
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }
        for (AclModuleLevelDto dto : aclModuleLevelList){
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)){
                Collections.sort(aclDtoList, aclSeqComparable);
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }
    }

    public List<AclModuleLevelDto> aclModuleTree(){
        List<SysAclModule> aclModuleList = sysPowerModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : aclModuleList){
            AclModuleLevelDto dto = AclModuleLevelDto.adapt(aclModule);
            dtoList.add(dto);
        }
        return aclModuleListToTree(dtoList);
    }

    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> aclModuleLevelDtoList){
        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)){
            return Lists.newArrayList();
        }
        // level -> [aclModule1, aclModule2, ...]
        Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto dto : aclModuleLevelDtoList) {
            levelAclModuleMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        Collections.sort(rootList, aclModuleSeqComparable);
        // 递归生成树
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
        return rootList;
    }

    private void transformAclModuleTree(List<AclModuleLevelDto> aclModuleLevelList, String level, Multimap<String, AclModuleLevelDto> levelAclModuleMap){
        for(int i = 0; i < aclModuleLevelList.size(); i++){
            // 遍历该层的每个元素
            AclModuleLevelDto aclModuleLevelDto = aclModuleLevelList.get(i);
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
            // 处理下一层
            List<AclModuleLevelDto> tempList = (List<AclModuleLevelDto>) levelAclModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempList)){
                // 排序
                Collections.sort(tempList, aclModuleSeqComparable);
                // 设置下一层部门
                aclModuleLevelDto.setAclModuleList(tempList);
                // 进入下一层处理
                transformAclModuleTree(tempList, nextLevel, levelAclModuleMap);
            }
        }
    }

    private Comparator<SysDeptDTO> deptSeqComparable = new Comparator<SysDeptDTO>() {
        @Override
        public int compare(SysDeptDTO o1, SysDeptDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    private Comparator<AclModuleLevelDto> aclModuleSeqComparable = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    private Comparator<AclDto> aclSeqComparable = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
