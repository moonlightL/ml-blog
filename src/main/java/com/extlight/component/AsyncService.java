package com.extlight.component;

import com.extlight.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author MoonlightL
 * @Title: AsyncService
 * @ProjectName ml-blog
 * @Description: 用于异步处理
 * @date 2018/6/15 0015 下午 12:33
 */
@Component
@Slf4j
public class AsyncService {

    private static final String BAIDU_PUSH_URL = "http://data.zz.baidu.com/urls?site=HOME_PAGE&token=TOKEN";

    @Autowired
    private CommonMap commonMap;

    /**
     * 百度推送
     * @param url
     */
    @Async
    public void push2Baidu(String url) {

        if (!StringUtils.isEmpty(commonMap.get("baidu_push_token"))) {
            String bd_push_token = commonMap.get("baidu_push_token").toString();
            String homePage = commonMap.get("homePage").toString();

            String requestUrl = BAIDU_PUSH_URL.replace("HOME_PAGE", homePage).replace("TOKEN", bd_push_token);
            String result = HttpClientUtil.sendPost(requestUrl, homePage + "/" + url);

            log.info("=====baidu push result:{}=====",result);
            return;
        }

        log.info("=====baidu_push_token 为空=====");

    }

}
