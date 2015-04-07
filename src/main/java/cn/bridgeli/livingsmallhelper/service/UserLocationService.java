package cn.bridgeli.livingsmallhelper.service;

import cn.bridgeli.livingsmallhelper.entity.UserLocation;

public interface UserLocationService {

    void save(UserLocation userLocation);

    UserLocation get(String open_id);

}
