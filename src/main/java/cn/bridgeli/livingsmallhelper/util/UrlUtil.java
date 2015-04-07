package cn.bridgeli.livingsmallhelper.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtil {

    private static final Logger LOG = LoggerFactory.getLogger(UrlUtil.class);

    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        HttpsURLConnection httpUrlConn = null;

        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            TrustManager[] tm = { new MyX509TrustManagerUtil() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            inputStream = httpUrlConn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException e) {
            LOG.error("ConnectException", e);
        } catch (IOException e) {
            LOG.error("IOException", e);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("NoSuchAlgorithmException", e);
        } catch (NoSuchProviderException e) {
            LOG.error("NoSuchProviderException", e);
        } catch (KeyManagementException e) {
            LOG.error("KeyManagementException", e);
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (IOException e) {
                LOG.error("IOException", e);
            }
            bufferedReader = null;
            inputStreamReader = null;
            inputStream = null;
            httpUrlConn.disconnect();
        }
        return jsonObject;
    }

    public static String httpRequest(String requestUrl) {
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpUrlConn = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(requestUrl);
            httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();
            // 将返回的输入流转换成字符串
            inputStream = httpUrlConn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
        } catch (MalformedURLException e) {
            LOG.error("MalformedURLException", e);
        } catch (ProtocolException e) {
            LOG.error("ProtocolException", e);
        } catch (IOException e) {
            LOG.error("IOException", e);
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                    bufferedReader = null;
                } catch (IOException e) {
                    LOG.error("IOException", e);
                }
            }
            if (null != inputStreamReader) {
                try {
                    inputStreamReader.close();
                    inputStreamReader = null;
                } catch (IOException e) {
                    LOG.error("IOException", e);
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    LOG.error("IOException", e);
                }
            }
            httpUrlConn.disconnect();
        }

        return buffer.toString();
    }

    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}