package com.extlight.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class IPUtil {

    private static final String OPEN_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=";

    /**
     * 通过 ip 获取地理位置详情
     * @param ip
     * @return
     */
    public static Map<String,Object> getIPInfo(String ip){
        String url = OPEN_URL + ip;
        String resultStr = HttpClientUtil.sendGet(url);

        try {
            Map<String,Object> result = JsonUtil.string2Obj(resultStr,Map.class);

            if ("0".equals(result.get("code").toString())) {
                return (Map<String, Object>) result.get("data");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 通过 ip 获取国家名
     * @param ip
     * @return
     */
    public static String getContry(String ip) {
        Map<String,Object> info = getIPInfo(ip);
        if ( info != null ) {
            return info.get("country").toString();
        }
        return null;
    }

    /**
     * 通过 ip 获取省份名
     * @return
     */
    public static String getProvince(String ip) {
        Map<String,Object> info = getIPInfo(ip);
        if ( info != null ) {
            return info.get("region").toString() + "省";
        }
        return null;
    }

    /**
     * 通过 ip 获取城市名
     * @param ip
     * @return
     */
    public static String getCity(String ip) {
        Map<String,Object> info = getIPInfo(ip);
        if ( info != null ) {
            return info.get("city").toString() + "市";
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
