package com.github.liuyueyi.forum.service.user.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.liueyueyi.forum.api.model.enums.YesOrNoEnum;
import com.github.liuyueyi.forum.service.user.repository.entity.UserDO;
import com.github.liuyueyi.forum.service.user.repository.entity.UserInfoDO;
import com.github.liuyueyi.forum.service.user.repository.mapper.UserInfoMapper;
import com.github.liuyueyi.forum.service.user.repository.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author YiHui
 * @date 2022/9/2
 */
@Repository
public class UserDao extends ServiceImpl<UserInfoMapper, UserInfoDO> {
    @Resource
    private UserMapper userMapper;

    /**
     * 三方账号登录方式
     *
     * @param accountId
     * @return
     */
    public UserDO getByThirdAccountId(String accountId) {
        return userMapper.getByThirdAccountId(accountId);
    }

    /**
     * 用户名登录
     *
     * @param userName
     * @return
     */
    public UserDO getByUserName(String userName) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        query.eq(UserDO::getUserName, userName)
                .eq(UserDO::getDeleted, YesOrNoEnum.NO.getCode());
        return userMapper.selectOne(query);
    }

    public void saveUser(UserDO user) {
        userMapper.insert(user);
    }

    public UserInfoDO getByUserId(Long userId) {
        LambdaQueryWrapper<UserInfoDO> query = Wrappers.lambdaQuery();
        query.eq(UserInfoDO::getUserId, userId)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode());
        return baseMapper.selectOne(query);
    }

    public Integer getUserCount() {
        return lambdaQuery()
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count().intValue();
    }

    public void updateUserInfo(UserInfoDO user) {
        UserInfoDO record = getByUserId(user.getUserId());
        if (record.equals(user)) {
            return;
        }
        if (StringUtils.isEmpty(user.getPosition())) {
            user.setPosition(null);
        }
        if (StringUtils.isEmpty(user.getCompany())) {
            user.setCompany(null);
        }
        if (StringUtils.isEmpty(user.getProfile())) {
            user.setProfile(null);
        }
        if (StringUtils.isEmpty(user.getPhoto())) {
            user.setPhoto(null);
        }
        if (StringUtils.isEmpty(user.getUserName())) {
            user.setUserName(null);
        }
        user.setId(record.getId());
        updateById(user);
    }
}
