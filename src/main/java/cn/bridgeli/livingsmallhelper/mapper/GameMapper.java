package cn.bridgeli.livingsmallhelper.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.bridgeli.livingsmallhelper.entity.Game;

public interface GameMapper {

    Game getLastGame(String fromUserName);

    void update(@Param("id") int id, @Param("gameStatus") int gameStatus, @Param("finishTime") Date finishTime);

    List<Map<Integer, Integer>> getScoreByOpenId(String fromUserName);

    int save(Game gameNew);

}
