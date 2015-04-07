package cn.bridgeli.livingsmallhelper.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.bridgeli.livingsmallhelper.service.CoreService;
import cn.bridgeli.livingsmallhelper.util.SignUtil;

@Controller
@RequestMapping("/coreController")
public class CoreController {

    private static final Logger LOG = LoggerFactory.getLogger(CoreController.class);

    @Resource
    private CoreService coreService;

    @RequestMapping(value = "/handleWeiXinMessage", method = RequestMethod.GET)
    public void handleWeiXinMessageForAccressAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.print(echostr);
            } catch (IOException e) {
                LOG.error("Access authentication fail", e);
            } finally {
                out.close();
                out = null;
            }
        }
    }

    @RequestMapping(value = "/handleWeiXinMessage", method = RequestMethod.POST)
    public void handleWeiXinMessage(HttpServletRequest request, HttpServletResponse response) {

        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");

        if (SignUtil.checkSignature(signature, timestamp, nonce)) {

            try {
                request.setCharacterEncoding("UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOG.error("Set encodeing fail", e);
            }
            response.setCharacterEncoding("UTF-8");

            String respMessage = coreService.processRequest(request);
            LOG.warn("respMessage = " + respMessage);
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.print(respMessage);
            } catch (IOException e) {
                LOG.error("Get write fail", e);
            } finally {
                out.close();
            }
        }
    }

}
