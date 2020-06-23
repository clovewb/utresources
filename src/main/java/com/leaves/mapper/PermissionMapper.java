package com.leaves.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.leaves.entity.Menu;
import com.leaves.entity.Permission;
import com.leaves.entity.SubSystemInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限操作
 *
 * @author zj
 * @version 1.0 @
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据UserId获取菜单信息
     *
     * @return
     */
    List<Menu> getMenuInfoByUserId(String userId);

    /**
     * @param userId
     * @return
     */
    List<SubSystemInfo> getSubSystemInfoByUserId(@Param(value = "userId") String userId);
    List<SubSystemInfo> getSubSystemInfo();
    /**
     * 根据角色查找所有权限
     *
     * @param roleId
     * @return
     */
    List<Permission> selectPermissionByRoleId(@Param(value = "roleId") String roleId);

    /**
     * 分配权限
     *
     * @param roleId
     * @param permissionId
     * @return
     */
    int allotPermission(@Param(value = "roleId") String roleId, @Param(value = "permissionId") String permissionId);

    /**
     * 删除角色下的权限
     *
     * @param permissionId
     * @param roleId
     * @return
     */
    int delPermission(@Param(value = "permissionId") String permissionId, @Param(value = "roleId") String roleId);

    /**
     * 根据权限id删除所有其权限的赋予
     *
     * @param permissionId
     * @return
     */
    int delPermissionByPermissionId(@Param(value = "permissionId") String permissionId);
}
