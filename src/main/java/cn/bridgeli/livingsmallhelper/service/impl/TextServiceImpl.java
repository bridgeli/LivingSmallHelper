package cn.bridgeli.livingsmallhelper.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.bridgeli.livingsmallhelper.entity.BusinessLogic;
import cn.bridgeli.livingsmallhelper.mapper.BusinessLogicMapper;
import cn.bridgeli.livingsmallhelper.service.ChatService;
import cn.bridgeli.livingsmallhelper.service.GameService;
import cn.bridgeli.livingsmallhelper.service.KeyService;
import cn.bridgeli.livingsmallhelper.service.TextService;
import cn.bridgeli.livingsmallhelper.util.GameUtil;

@Service
public class TextServiceImpl implements TextService {

    private static final Logger LOG = LoggerFactory.getLogger(TextServiceImpl.class);

    @Resource
    private BusinessLogicMapper businessLogicMapper;
    @Resource
    private ChatService chatService;
    @Resource
    private GameService gameService;

    @Override
    public String handleText(String content, String fromUserName) {
        List<BusinessLogic> businessLogics = businessLogicMapper.query();

        boolean isFind = false;
        String respMsg = null;
        for (BusinessLogic businessLogic : businessLogics) {

            if (isFind) {
                break;
            }

            String[] keys = businessLogic.getKeyWord().split("@");

            for (String k : keys) {
                if (content.startsWith(k)) {
                    String className = businessLogic.getClassname();
                    KeyService keyService = null;
                    try {
                        keyService = (KeyService) Class.forName(className).newInstance();
                    } catch (InstantiationException e) {
                        LOG.error("InstantiationException", e);
                    } catch (IllegalAccessException e) {
                        LOG.error("IllegalAccessException", e);
                    } catch (ClassNotFoundException e) {
                        LOG.error("ClassNotFoundException", e);
                    }
                    String temp = content.substring(k.length());
                    if (null == temp || "".equals(temp)) {
                        respMsg = keyService.getUsage();
                    } else {
                        respMsg = keyService.excute(temp);
                    }
                    isFind = true;
                    break;
                }
            }
        }
        if (null == respMsg || "".equals(respMsg)) {
            if (GameUtil.verifyNo(content) && !GameUtil.verifyRepeat(content)) {
                respMsg = gameService.process(content, fromUserName);
            } else if ("score".equals(content)) {
                respMsg = gameService.getScore(fromUserName);
            } else {
                respMsg = chatService.chat(content, fromUserName);
            }
        }
        return respMsg;
    }
}
