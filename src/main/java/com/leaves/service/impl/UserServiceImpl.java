package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.*;
import com.leaves.mapper.*;
import com.leaves.service.UserService;
import com.leaves.util.DateUtil;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户service实现类
 *
 * @author
 * @create 2019/10/7 15:40
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private IntegrateUtils itdragonUtils;
    @Resource
    private PostInfoMapper postInfoMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private AttentionMapper attentionMapper;

    @Override
    public User findUserByUserName(String userName) {
        User searchUser = new User();
        searchUser.setUserName(userName);
        User user = userMapper.selectOne(searchUser);
        if (user != null) {
            List<Role> roleList = roleMapper.selectRoleListByUserId(user.getId());
            if (!roleList.isEmpty()) {
                for (Role role : roleList) {
                    List<Permission> permissionList = permissionMapper.selectPermissionByRoleId(role.getId());
                    role.setPermissions(permissionList);
                }
            }
            user.setRoleList(roleList);
            return user;
        }
        return null;
    }

    @Override
    public User getUserByUserName(String userName) {
        User searchUser = new User();
        searchUser.setUserName(userName);
        User user = userMapper.selectOne(searchUser);
        return user;
    }

    @Override
    public Page<User> getUserList(User user, int page, int limit) {
        EntityWrapper<User> searchInfo = new EntityWrapper<>();
        if (user != null) {
            if (user.getUserName() != null && !"".equals(user.getUserName())) {
                searchInfo.eq("userName", user.getUserName());
            }
            if (user.getUserName() != null && !"".equals(user.getIphone())) {
                searchInfo.eq("iphone", user.getIphone());
            }
            if (user.getStatus() != null && !"".equals(user.getStatus())) {
                searchInfo.eq("status", user.getStatus());
            }
        }
        searchInfo.ne("userName", "adminStrator");
        searchInfo.orderBy("createdDate desc");
        Page<User> pageInfo = new Page<>(page, limit);
        List<User> userList = userMapper.selectPage(pageInfo, searchInfo);
        if (!userList.isEmpty()) {
            pageInfo.setRecords(userList);
        }
        return pageInfo;
    }

    @Override
    public Page<User> getUserListOrderByPostNum(User user, int page, int limit) {
        EntityWrapper<User> searchInfo = new EntityWrapper<>();
        searchInfo.ne("userName", "adminStrator");
        searchInfo.orderBy("postNum desc");
        Page<User> pageInfo = new Page<>(page, limit);
        List<User> userList = userMapper.selectPage(pageInfo, searchInfo);
        if (!userList.isEmpty()) {
            pageInfo.setRecords(userList);
        }
        return pageInfo;
    }

    @Override
    public Page<User> getUserListForRole(User user, int page, int limit, String roleId) {
        EntityWrapper<User> searchInfo = new EntityWrapper<>();
        if (user != null) {
            if (user.getStatus() != null && !"".equals(user.getStatus())) {
                searchInfo.eq("status", user.getStatus());
            }
        }
        searchInfo.ne("userName", "adminStrator");
        searchInfo.orderBy("createdDate desc");
        Page<User> pageInfo = new Page<>(page, limit);
        List<User> userList = userMapper.selectPage(pageInfo, searchInfo);
        List<String> userIdList = roleMapper.slectRoleAndUser(roleId);
        List<User> resultList = new ArrayList<>();
        for (User user1 : userList) {
            if (!userIdList.contains(user1.getId())) {
                resultList.add(user1);
            }
        }
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public Page<User> getUserListByRoleId(String roleId, int page, int limit) {
        Page<User> pageInfo = new Page<>(page, limit);
        List<User> userList = userMapper.selectUserListByRoleId(pageInfo, roleId);
        if (!userList.isEmpty()) {
            pageInfo.setRecords(userList);
        }
        return pageInfo;
    }

    @Override
    public boolean changePass(String newPas, String userName) {
        User user = new User();
        user.setPassword(newPas);
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("userName", userName);
        if (IntegrateUtils.isEncrypted()) {
            user.setPlainPassword(newPas);
            itdragonUtils.entryptPassword(user);
        }
        int result = userMapper.update(user, entityWrapper);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateUserStatus(String id, Integer status) {
        User user = new User();
        user.setStatus(status);
        user.setId(id);
        int result = userMapper.updateById(user);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean insert(User user) {
        user.setStatus(1);
        user.setCreatedDate(DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        if (IntegrateUtils.isEncrypted()) {
            itdragonUtils.entryptPassword(user);
        } else {
            user.setPassword(user.getPlainPassword());
        }
        user.setPostNum(0);
        int result = userMapper.insert(user);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateLoginTime(String userName, String time) {
        User user = new User();
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        user.setUpdatedDate(time);
        entityWrapper.eq("userName", userName);
        userMapper.update(user, entityWrapper);
    }

    @Override
    public List<User> selectList() {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.ne("userName", "adminStrator");
        entityWrapper.ne("userName", "admin");
        return userMapper.selectList(entityWrapper);
    }

    @Override
    public List<User> selectList(String UserName) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.ne("userName", "adminStrator");
        entityWrapper.like("userName", UserName);
        return userMapper.selectList(entityWrapper);

    }

    @Override
    public boolean isAdmin(String userId) {
        List<Role> roleList = roleMapper.selectRoleListByUserId(userId);
        for (Role role : roleList) {
            if ("管理员".equals(role.getRole())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isUser(String userId) {
        List<Role> roleList = roleMapper.selectRoleListByUserId(userId);
        for (Role role : roleList) {
            if ("学生".equals(role.getRole())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String zumNumber() {
        return userMapper.selectList(null).size() + "";
    }

    @Override
    public boolean insertAttention(String attenId) {
        Attention attention = new Attention();
        attention.setAttenId(attenId);
        attention.setUserId(itdragonUtils.getSessionUser().getId());
        Integer insert = attentionMapper.insert(attention);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delAttention(String attenId) {
        EntityWrapper<Attention> objectEntityWrapper = new EntityWrapper<>();
        objectEntityWrapper.eq("userId", itdragonUtils.getSessionUser().getId());
        objectEntityWrapper.eq("attenId", attenId);
        Integer delete = attentionMapper.delete(objectEntityWrapper);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAttention(String attenId) {
        Attention attention = new Attention();
        attention.setAttenId(attenId);
        attention.setUserId(itdragonUtils.getSessionUser().getId());
        Attention one = attentionMapper.selectOne(attention);
        if (one != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<Attention> getAttentionList(String userId) {
        EntityWrapper<Attention> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId);
        List<Attention> collectList = attentionMapper.selectList(wrapper);
        for (Attention attention : collectList) {
            attention.setUser(userMapper.selectById(attention.getAttenId()));
        }
        return collectList;
    }

    @Override
    public List<Attention> getFenAttentionList(String attenId) {
        EntityWrapper<Attention> wrapper = new EntityWrapper<>();
        wrapper.eq("attenId", attenId);
        List<Attention> collectList = attentionMapper.selectList(wrapper);
        for (Attention attention : collectList) {
            attention.setUser(userMapper.selectById(attention.getUserId()));
        }
        return collectList;
    }

    @Override
    public boolean deleteByPrimaryKey(String ids) {
        String[] idList = ids.split(",");
        int result = 0;
        for (String s : idList) {
            result = userMapper.deleteById(s);
            //删除用户后删除用户发布的视频
            EntityWrapper<PostInfo> wrapper = new EntityWrapper<>();
            wrapper.eq("userId", s);
            postInfoMapper.delete(wrapper);
            //删除用户的评论
            EntityWrapper<Comment> wrapper1 = new EntityWrapper<>();
            wrapper1.eq("userId", s);
            commentMapper.delete(wrapper1);
            if (result > 0) {
                roleMapper.delUserByUserId(s);
            }
        }
        if (result > 0) {

            return true;
        } else {
            return false;
        }
    }

    @Override
    public User selectByPrimaryKey(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean updateByPrimaryKey(User user) {
        if (IntegrateUtils.isEncrypted()) {
            itdragonUtils.entryptPassword(user);
        } else {
            user.setPassword(user.getPlainPassword());
        }
        int result = userMapper.updateById(user);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


}