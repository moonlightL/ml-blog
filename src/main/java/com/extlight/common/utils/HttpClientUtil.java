package com.extlight.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientUtil {

    private static PoolingHttpClientConnectionManager cm;

    static {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(20);
        // 启动该线程，负责清理无效连接
        new IdleConnectionEvictor(cm).start();
    }

    public static class IdleConnectionEvictor extends Thread {

        private final HttpClientConnectionManager connMgr;

        private volatile boolean shutdown;

        public IdleConnectionEvictor(HttpClientConnectionManager connMgr) {
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        // 关闭失效的连接
                        System.out.println("======");
                        connMgr.closeExpiredConnections();
                    }
                }
            } catch (InterruptedException ex) {
                // 结束
            }
        }
    }

    /**
     * 发送 GET 请求
     * @param url
     * @return
     */
    public static String sendGet(String url) {
        // 创建 httpClient
        CloseableHttpClient httpclient = HttpClients.custom()
                                                    .setConnectionManager(cm)
                                                    .build();
        // 创建 http 请求
        HttpGet httpGet = new HttpGet(url);

        // 设置请求配置
        httpGet.setConfig(getRequestConfig());

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectTimeout(1000) // 创建连接的最长时间
                                      .setConnectionRequestTimeout(500) // 从连接池获取连接的最长时间
                                      .setSocketTimeout(10000) // 数据传输最长时间
                                      .build();
    }

}
