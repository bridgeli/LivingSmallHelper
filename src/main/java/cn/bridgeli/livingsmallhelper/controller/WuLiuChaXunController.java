package cn.bridgeli.livingsmallhelper.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/wuLiuChaXunController")
public class WuLiuChaXunController {

    private static final Logger LOG = LoggerFactory.getLogger(WuLiuChaXunController.class);

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public void search(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String compname = request.getParameter("compname");
        String dingdanhao = request.getParameter("dingdanhao");
        String requestUrl = "http://m.kuaidi100.com/index_all.html?type=COMPNAME&postid=DINGDANHAO&callbackurl=http://livingsmallhelper.sinaapp.com/wuliu.html";
        requestUrl = requestUrl.replace("COMPNAME", URLEncoder.encode(compname, "UTF-8").replace("DINGDANHAO", dingdanhao));
        try {
            response.sendRedirect(requestUrl);
        } catch (IOException e) {
            LOG.warn("requestUrl = " + requestUrl);
            LOG.error("IOException = " + e);
        }
    }
}
