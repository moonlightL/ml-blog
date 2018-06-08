package com.extlight.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class IPUtil {

    private static final String OPEN_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";

    /**
     * 通过 ip 获取地理位置详情
     * @param ip
     * @return
     */
    public static Map<String,String> getLocationInfo(String ip){
        String url = OPEN_URL + ip;

        String result = HttpClientUtil.sendGet(url);

        try {
            Map<String,String> map = JsonUtil.string2Obj(result, Map.class);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过 ip 获取国家名
     * @param ip
     * @return
     */
    public static String getContry(String ip) {
        Map<String, String> locationInfo = getLocationInfo(ip);
        if ( locationInfo != null ) {
            return locationInfo.get("country");
        }
        return null;
    }

    /**
     * 通过 ip 获取省份名
     * @return
     */
    public static String getProvince(String ip) {
        Map<String, String> locationInfo = getLocationInfo(ip);
        if ( locationInfo != null ) {
            return locationInfo.get("province") + "省";
        }
        return null;
    }

    /**
     * 通过 ip 获取城市名
     * @param ip
     * @return
     */
    public static String getCity(String ip) {
        Map<String, String> locationInfo = getLocationInfo(ip);
        if ( locationInfo != null ) {
            return locationInfo.get("city") + "市";
        }
        return null;
    }

    /**
     * 获取客户端真实IP
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {

        String ipAddress = request.getHeader("x-forwarded-for");

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {

            ipAddress = request.getRemoteAddr();

            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
