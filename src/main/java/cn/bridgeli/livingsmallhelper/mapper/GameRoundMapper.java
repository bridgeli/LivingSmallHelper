package cn.bridgeli.livingsmallhelper.mapper;

import java.util.List;

import cn.bridgeli.livingsmallhelper.entity.GameRound;

public interface GameRoundMapper {

    void save(GameRound gameRound);

    List<GameRound> queryByOpenId(int id);

}
