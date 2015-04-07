package cn.bridgeli.livingsmallhelper.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import cn.bridgeli.livingsmallhelper.message.resp.Music;
import cn.bridgeli.livingsmallhelper.message.resp.MusicMessage;
import cn.bridgeli.livingsmallhelper.message.resp.TextMessage;
import cn.bridgeli.livingsmallhelper.service.BaiduMusicService;
import cn.bridgeli.livingsmallhelper.util.MessageUtil;
import cn.bridgeli.livingsmallhelper.util.UrlUtil;

@Service
public class BaiduMusicServiceImpl implements BaiduMusicService {

    @Override
    public String searchMusic(String song, String fromUserName, String toUserName, TextMessage textMessage) {
        String[] kwArr = song.split("@");
        // 歌曲名称
        String musicTitle = kwArr[0];
        // 演唱者默认为空
        String musicAuthor = "";
        if (2 == kwArr.length) {
            musicAuthor = kwArr[1];
        }
        // 百度音乐搜索地址
        String requestUrl = "http://box.zhangmen.baidu.com/x?op=12&count=1&title={TITLE}$${AUTHOR}$$$$";
        // 对音乐名称、作者进URL编码
        requestUrl = requestUrl.replace("{TITLE}", UrlUtil.urlEncodeUTF8(musicTitle));
        requestUrl = requestUrl.replace("{AUTHOR}", UrlUtil.urlEncodeUTF8(musicAuthor));
        // 处理名称、作者中间的空格
        requestUrl = requestUrl.replaceAll("\\+", "%20");

        // 查询并获取返回结果
        String result = UrlUtil.httpRequest(requestUrl);
        byte[] bytes = null;
        try {
            bytes = result.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        InputStream inputStream = new ByteArrayInputStream(bytes);
        // 从返回结果中解析出Music
        Music music = parseMusic(inputStream);

        // 如果music不为null，设置标题和描述
        if (null != music) {
            music.setTitle(musicTitle);
            // 如果作者不为""，将描述设置为作者
            if (!"".equals(musicAuthor))
                music.setDescription(musicAuthor);
            else
                music.setDescription("来自生活小助手");
        }

        String respMessage = "";
        if (null != music) {
            // 音乐消息
            MusicMessage musicMessage = new MusicMessage();
            musicMessage.setToUserName(fromUserName);
            musicMessage.setFromUserName(toUserName);
            musicMessage.setCreateTime(new Date().getTime());
            musicMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_MUSIC);
            musicMessage.setMusic(music);
            respMessage = MessageUtil.musicMessageToXml(musicMessage);
        } else {
            textMessage.setContent("对不起，没有找到你想听的歌曲<" + musicTitle + ">。");
            respMessage = MessageUtil.textMessageToXml(textMessage);
        }
        return respMessage;
    }

    private static Music parseMusic(InputStream inputStream) {
        Music music = null;
        try {
            // 使用dom4j解析xml字符串
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // count表示搜索到的歌曲数
            String count = root.element("count").getText();
            // 当搜索到的歌曲数大于0时
            if (!"0".equals(count)) {
                // 普通品质
                List<Element> urlList = root.elements("url");
                // 高品质
                List<Element> durlList = root.elements("durl");

                // 普通品质的encode、decode
                String urlEncode = urlList.get(0).element("encode").getText();
                String urlDecode = urlList.get(0).element("decode").getText();
                // 普通品质音乐的URL
                String url = urlEncode.substring(0, urlEncode.lastIndexOf("/") + 1) + urlDecode;
                if (-1 != urlDecode.lastIndexOf("&"))
                    url = urlEncode.substring(0, urlEncode.lastIndexOf("/") + 1) + urlDecode.substring(0, urlDecode.lastIndexOf("&"));

                // 默认情况下，高音质音乐的URL 等于 普通品质音乐的URL
                String durl = url;

                // 判断高品质节点是否存在
                Element durlElement = durlList.get(0).element("encode");
                if (null != durlElement) {
                    // 高品质的encode、decode
                    String durlEncode = durlList.get(0).element("encode").getText();
                    String durlDecode = durlList.get(0).element("decode").getText();
                    // 高品质音乐的URL
                    durl = durlEncode.substring(0, durlEncode.lastIndexOf("/") + 1) + durlDecode;
                    if (-1 != durlDecode.lastIndexOf("&"))
                        durl = durlEncode.substring(0, durlEncode.lastIndexOf("/") + 1) + durlDecode.substring(0, durlDecode.lastIndexOf("&"));
                }
                music = new Music();
                // 设置普通品质音乐链接
                music.setMusicUrl(url);
                // 设置高品质音乐链接
                music.setHQMusicUrl(durl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return music;
    }

}
