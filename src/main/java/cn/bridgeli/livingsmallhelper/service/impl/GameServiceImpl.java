package cn.bridgeli.livingsmallhelper.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.bridgeli.livingsmallhelper.entity.Game;
import cn.bridgeli.livingsmallhelper.entity.GameRound;
import cn.bridgeli.livingsmallhelper.mapper.GameMapper;
import cn.bridgeli.livingsmallhelper.mapper.GameRoundMapper;
import cn.bridgeli.livingsmallhelper.service.GameService;
import cn.bridgeli.livingsmallhelper.util.GameUtil;

@Service
public class GameServiceImpl implements GameService {

    @Resource
    private GameMapper gameMapper;
    @Resource
    private GameRoundMapper gameRoundMapper;

    @Override
    public String process(String content, String fromUserName) {
        Game game = gameMapper.getLastGame(fromUserName);
        String gameAnswer = null;
        String quessResult = null;
        int id = -1;
        // 新的一局
        if (null == game || 0 != game.getGameStatus()) {
            gameAnswer = GameUtil.generateRandNo();
            quessResult = GameUtil.quessResult(gameAnswer, content);
            Game gameNew = new Game();
            gameNew.setOpenId(fromUserName);
            gameNew.setGameAnswer(gameAnswer);
            gameNew.setCreateTime(new Date());
            gameNew.setGameStatus(0);
            gameMapper.save(gameNew);
            id = gameNew.getId();
        } else {
            gameAnswer = game.getGameAnswer();
            quessResult = GameUtil.quessResult(gameAnswer, content);
            id = game.getId();
        }
        // 保存历史记录
        GameRound gameRound = new GameRound();
        gameRound.setGameId(id);
        gameRound.setOpenId(fromUserName);
        gameRound.setQuessNo(content);
        gameRound.setQuessTime(new Date());
        gameRound.setQuessResult(quessResult);

        gameRoundMapper.save(gameRound);

        List<GameRound> gameRounds = gameRoundMapper.queryByOpenId(id);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("查看战绩请回复：score").append("\n");

        for (int i = 0; i < gameRounds.size(); i++) {
            gameRound = gameRounds.get(i);
            stringBuffer.append(String.format("第%d回合：%s %s", i + 1, gameRound.getQuessNo(), gameRound.getQuessResult())).append("\n");
        }
        if ("4A0B".equals(quessResult)) {
            stringBuffer.append("正确答案：").append(gameAnswer).append("\n");
            stringBuffer.append("恭喜您猜对了！[强]").append("\n");
            stringBuffer.append("请输入4个不重复的数字开始新的一局。");
            gameMapper.update(id, 1, new Date());
        } else if (gameRounds.size() >= 10) {
            stringBuffer.append("正确答案：").append(gameAnswer).append("\n");
            stringBuffer.append("很抱歉，10回合您仍没才出来，本局结束。。。[流泪]").append("\n");
            stringBuffer.append("请输入4个不重复的数字开始新的一局。");
            gameMapper.update(id, 2, new Date());
        } else {
            stringBuffer.append("请再接再厉！[加油]");
        }
        return stringBuffer.toString();
    }

    @Override
    public String getScore(String fromUserName) {
        StringBuffer stringBuffer = new StringBuffer();
        List<Map<Integer, Integer>> results = gameMapper.getScoreByOpenId(fromUserName);
        if (null == results || 0 == results.size()) {
            stringBuffer.append("您还没有玩过游戏“猜数字”").append("\n");
            stringBuffer.append("请输入4个不重复的数字开始新的一局，例如：0269");
        } else {
            Map<Integer, Integer> score = conversion(results);
            stringBuffer.append("您的游戏战绩如下：").append("\n");
            for (Integer status : score.keySet()) {
                if (0 == status) {
                    stringBuffer.append("游戏中：").append(score.get(status)).append("\n");
                } else if (1 == status) {
                    stringBuffer.append("胜利局数：").append(score.get(status)).append("\n");
                } else {
                    stringBuffer.append("失败局数：").append(score.get(status)).append("\n");
                }
            }
            stringBuffer.append("请输入4个不重复的数字开始新的一局，例如：0269");
        }
        return stringBuffer.toString();
    }

    private Map<Integer, Integer> conversion(List<Map<Integer, Integer>> results) {
        List<Integer> temp = new ArrayList<Integer>();
        for (Map<Integer, Integer> map : results) {
            for (Map.Entry<Integer, Integer> m : map.entrySet()) {
                temp.add(Integer.parseInt(m.getValue() + ""));
            }
        }
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (int i = 0; i < temp.size() - 1; i += 2) {
            result.put(temp.get(i), temp.get(i + 1));
        }
        return result;

    }
}
