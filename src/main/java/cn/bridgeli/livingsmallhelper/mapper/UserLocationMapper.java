package cn.bridgeli.livingsmallhelper.mapper;

import cn.bridgeli.livingsmallhelper.entity.UserLocation;

public interface UserLocationMapper {

    void add(UserLocation userLocation);

    UserLocation getLastLocation(String open_id);

}
