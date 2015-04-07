package cn.bridgeli.livingsmallhelper.mapper;

import cn.bridgeli.livingsmallhelper.entity.ChatLog;

public interface ChatLogMapper {
    void addChatLog(ChatLog chatLog);

    int getLastCategory(String fromUserName);

}
