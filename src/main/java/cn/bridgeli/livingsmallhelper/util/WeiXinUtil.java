package cn.bridgeli.livingsmallhelper.util;

import java.io.IOException;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeiXinUtil {
    public static String getFaceUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("人脸检测使用指南").append("\n\n");
        buffer.append("发送一张清晰的照片，就能帮你分析出种族、年龄、性别等信息").append("\n");
        buffer.append("快来试试你是不是长得太着急");
        return buffer.toString();
    }

    public static String getWeatherUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("天气预报使用指南").append("\n\n");
        buffer.append("回复天气+城市名称").append("\n");
        buffer.append("例如：天气郑州");
        return buffer.toString();
    }

    public static String getBusUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("公交查询使用指南").append("\n\n");
        buffer.append("查询城市公交线路").append("\n");
        buffer.append("格式：公交城市@线路名称").append("\n");
        buffer.append("例如：公交郑州@1路").append("\n");
        buffer.append("查询城市公交驾乘方案").append("\n\n");
        buffer.append("格式：公交城市@起点至终点").append("\n");
        buffer.append("例如：公交郑州@火车站至河南财经学院");
        return buffer.toString();
    }

    public static String getNearSearchUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("周边搜索使用指南").append("\n\n");
        buffer.append("1）发送地理位置").append("\n");
        buffer.append("点击窗口底部的“+”按钮，选择“位置”，点“发送”").append("\n");
        buffer.append("2）指定关键字搜索").append("\n\n");
        buffer.append("格式：附近+关键字").append("\n");
        buffer.append("例如：附近ATM、附近KTV、附近厕所\n\n");
        buffer.append("温馨提醒：第一次使用或者在新地点使用本功能，请先发送位置");
        return buffer.toString();
    }

    public static String getMoviceUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("电影排行榜使用指南").append("\n\n");
        buffer.append("格式：电影+地点").append("\n");
        buffer.append("例如：电影郑州");
        return buffer.toString();
    }

    public static String getSongUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("歌曲点播操作指南").append("\n\n");
        buffer.append("格式：歌曲+歌名").append("\n");
        buffer.append("例如：歌曲放心去飞").append("\n");
        buffer.append("或者：歌曲放心去飞@小虎队");
        return buffer.toString();
    }

    public static String getGuessNumberUsage() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("《猜数字游戏玩法》").append("\n\n");
        buffer.append("系统设定一个没有重复数字的4位数，由玩家来猜，每局10次机会。").append("\n");
        buffer.append("每猜一次，系统会给出猜测结果xAyB，x表示数字与位置均正确的数的个数，y表示数字正确但位置不对的数的个数。").append("\n");
        buffer.append("玩家根据猜测结果xAyB一直猜，直到猜中(4A0B)为止。").append("\n");
        buffer.append("如果10次都没猜中，系统会公布答案，游戏结束。").append("\n");
        buffer.append("玩家任意输入一个没有重复数字的4位数即开始游戏，例如：7890");
        return buffer.toString();
    }

    public static String getChatUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("聊天唠嗑使用指南").append("\n\n");
        buffer.append("闲暇无聊，来找生活小助手唠嗑吧，生活小助手很能聊的，有问必答！例如：").append("\n");
        buffer.append("讲个笑话").append("\n");
        buffer.append("河南有什么好玩的").append("\n");
        buffer.append("订票电话").append("\n");
        buffer.append("周口的区号");
        return buffer.toString();
    }

    public static String getTranslationUsage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("翻译使用指南").append("\n\n");
        buffer.append("生活小助手为用户提供专业的多语言翻译服务，目前支持一下翻译方向：").append("\n");
        buffer.append("    中 -> 英").append("\n");
        buffer.append("    英 -> 中").append("\n");
        buffer.append("    日 -> 中").append("\n\n");
        buffer.append("使用示例：").append("\n");
        buffer.append("    翻译我是中国人").append("\n");
        buffer.append("    翻译dream").append("\n");
        buffer.append("    翻译釣魚島は中国のものだ").append("\n");
        return buffer.toString();
    }

    public static String getTodayInHistory() {
        String url = "http://www.rijiben.com/";

        Document doc = null;
        String text = "";
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != doc) {
            Elements listrens = doc.getElementsByAttributeValue("class", "listren");
            StringBuffer stringBuffer = new StringBuffer();
            for (Element listren : listrens) {
                stringBuffer.append(listren.select("li").select("a").html());
            }
            text = stringBuffer.toString();
        } else {
            text = "很抱歉，网络出异常,获取历史上的今天失败，请稍后再试";
        }
        return text;
    }

    public static String getFriend() {
        return "很抱歉，该功能正在开发中，稍后即将上线，敬请期待！";
    }

    public static String getNearSearch() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[愉快]").append("生活小助手，已成功接收您的位置").append("\n\n");
        buffer.append("您可以输入搜索关键词获取周边信息了，例如：").append("\n");
        buffer.append("    附近ATM").append("\n");
        buffer.append("    附近ＫＴＶ").append("\n");
        buffer.append("    附近厕所").append("\n");
        buffer.append("    附近电影院").append("\n");
        buffer.append("务必以“附近”两个字打头！");
        return buffer.toString();
    }

    public static String getDefaultAnswer() {
        String[] answers = { "要不我们聊点别的吧？", "嗯？您到底在说什么呢？", "没有听懂你说的，能否换个说法？", "虽然不明白您的意思，但我却能用心去感受", "听得我一头雾水，阁下的知识真是渊博啊，膜拜ing", "真心听不懂你在说什么，要不您换种表达方式如何？", "哎，实话说了吧，我小学语文老师是体育老师教的，理解起来有点困难哦", "是这个世界变化太快，还是我不够有才？为何您说的我永不明白？" };
        return answers[getRandomNO(answers.length)];
    }

    private static int getRandomNO(int length) {
        Random random = new Random();
        return random.nextInt(length);
    }
}
