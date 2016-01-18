package cn.bridgeli.livingsmallhelper.menu.service;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.bridgeli.livingsmallhelper.accesstoken.service.AccessTokenService;
import cn.bridgeli.livingsmallhelper.entity.Button;
import cn.bridgeli.livingsmallhelper.entity.CommonButton;
import cn.bridgeli.livingsmallhelper.entity.ComplexButton;
import cn.bridgeli.livingsmallhelper.entity.Menu;
import cn.bridgeli.livingsmallhelper.entity.ViewButton;
import cn.bridgeli.livingsmallhelper.util.UrlUtil;

public class MenuService {

    private static final Logger LOG = LoggerFactory.getLogger(MenuService.class);

    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;

        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        String jsonMenu = JSONObject.fromObject(menu).toString();
        JSONObject jsonObject = UrlUtil.httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                LOG.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // 第三方用户唯一凭证
        String appId = "XXXXXXXXXXXXXXXXXX";
        // 第三方用户唯一凭证密钥
        String appSecret = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        // 调用接口获取access_token
        String accessToken = AccessTokenService.getAccessToken(appId, appSecret);

        if (null != accessToken) {
            // 调用接口创建菜单
            int result = createMenu(getMenu(), accessToken);

            // 判断菜单创建结果
            if (0 == result)
                LOG.info("菜单创建成功！");
            else
                LOG.info("菜单创建失败，错误码：" + result);
        }
    }

    private static Menu getMenu() {
        CommonButton btn11 = new CommonButton();
        btn11.setName("天气预报");
        btn11.setType("click");
        btn11.setKey("11");

        CommonButton btn12 = new CommonButton();
        btn12.setName("公交查询");
        btn12.setType("click");
        btn12.setKey("12");

        CommonButton btn13 = new CommonButton();
        btn13.setName("周边搜索");
        btn13.setType("click");
        btn13.setKey("13");

        ViewButton btn14 = new ViewButton();
        btn14.setName("快递查询");
        btn14.setType("view");
        btn14.setUrl("http://m.kuaidi100.com");

        CommonButton btn15 = new CommonButton();
        btn15.setName("电影排行榜");
        btn15.setType("click");
        btn15.setKey("15");

        CommonButton btn21 = new CommonButton();
        btn21.setName("歌曲点播");
        btn21.setType("click");
        btn21.setKey("21");

        CommonButton btn22 = new CommonButton();
        btn22.setName("猜数字");
        btn22.setType("click");
        btn22.setKey("22");

        ViewButton btn23 = new ViewButton();
        btn23.setName("挑战2048");
        btn23.setType("view");
        btn23.setUrl("http://www.duopao.com/games/play/g20140812175834879362");

        CommonButton btn24 = new CommonButton();
        btn24.setName("人脸识别");
        btn24.setType("click");
        btn24.setKey("24");

        CommonButton btn25 = new CommonButton();
        btn25.setName("聊天唠嗑");
        btn25.setType("click");
        btn25.setKey("25");

        CommonButton btn31 = new CommonButton();
        btn31.setName("老友圈");
        btn31.setType("click");
        btn31.setKey("31");

        CommonButton btn32 = new CommonButton();
        btn32.setName("幽默笑话");
        btn32.setType("click");
        btn32.setKey("32");

        CommonButton btn33 = new CommonButton();
        btn33.setName("智能翻译");
        btn33.setType("click");
        btn33.setKey("33");

        CommonButton btn34 = new CommonButton();
        btn34.setName("历史上的今天");
        btn34.setType("click");
        btn34.setKey("34");

        ViewButton btn35 = new ViewButton();
        btn35.setName("关于作者");
        btn35.setType("view");
        btn35.setUrl("http://www.bridgeli.cn/%E5%85%B3%E4%BA%8E%E5%8D%9A%E4%B8%BB");

        ComplexButton mainBtn1 = new ComplexButton();
        mainBtn1.setName("生活助手");
        mainBtn1.setSub_button(new Button[] { btn11, btn12, btn13, btn14, btn15 });

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("休闲驿站");
        mainBtn2.setSub_button(new Button[] { btn21, btn22, btn23, btn24, btn25 });

        ComplexButton mainBtn3 = new ComplexButton();
        mainBtn3.setName("更多体验");
        mainBtn3.setSub_button(new Button[] { btn31, btn32, btn33, btn34, btn35 });

        /**
         * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
         * 
         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
         */
        Menu menu = new Menu();
        menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

        return menu;
    }
}
