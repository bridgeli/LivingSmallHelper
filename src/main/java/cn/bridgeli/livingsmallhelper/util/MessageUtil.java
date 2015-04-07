package cn.bridgeli.livingsmallhelper.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.bridgeli.livingsmallhelper.message.resp.Article;
import cn.bridgeli.livingsmallhelper.message.resp.MusicMessage;
import cn.bridgeli.livingsmallhelper.message.resp.NewsMessage;
import cn.bridgeli.livingsmallhelper.message.resp.TextMessage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class MessageUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MessageUtil.class);

    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    public static final String EVENT_TYPE_CLICK = "CLICK";

    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                boolean cdata = true;

                @SuppressWarnings("rawtypes")
                @Override
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    public static String musicMessageToXml(MusicMessage musicMessage) {
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

    public static String newsMessageToXml(NewsMessage newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new Article().getClass());
        return xstream.toXML(newsMessage);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();

        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();

            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }
        } catch (IOException e) {
            LOG.error("IOException", e);
        } catch (DocumentException e) {
            LOG.error("DocumentException", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOG.error("IOException", e);
            }
            inputStream = null;
        }

        return map;
    }

    public static String textMessageToXml(TextMessage textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }
}
