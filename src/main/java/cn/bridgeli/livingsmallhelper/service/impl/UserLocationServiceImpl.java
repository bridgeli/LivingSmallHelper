package cn.bridgeli.livingsmallhelper.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.bridgeli.livingsmallhelper.entity.UserLocation;
import cn.bridgeli.livingsmallhelper.mapper.UserLocationMapper;
import cn.bridgeli.livingsmallhelper.service.UserLocationService;

@Service
public class UserLocationServiceImpl implements UserLocationService {

    @Resource
    private UserLocationMapper userLocationMapper;

    @Override
    public void save(UserLocation userLocation) {
        userLocationMapper.add(userLocation);
    }

    @Override
    public UserLocation get(String open_id) {
        return userLocationMapper.getLastLocation(open_id);
    }

}
