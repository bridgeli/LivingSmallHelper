package cn.bridgeli.livingsmallhelper.service;

public interface GameService {

    String process(String content, String fromUserName);

    String getScore(String fromUserName);
}
