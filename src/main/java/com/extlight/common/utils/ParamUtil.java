package com.extlight.common.utils;

import com.extlight.common.constant.ParamConstant;
import com.extlight.component.CommonMap;
import com.extlight.web.context.SpringContext;
import org.springframework.util.StringUtils;

public class ParamUtil {

    public static boolean checkParameter(int type) {

        CommonMap commonMap = SpringContext.getBeanByName("commonMap");

        if (type == ParamConstant.QINIU) {// 七牛云
            if (StringUtils.isEmpty(commonMap.get("qn_accessKey"))
                    || StringUtils.isEmpty(commonMap.get("qn_secretKey"))
                    || StringUtils.isEmpty(commonMap.get("qn_bucket"))) {
                return false;
            }
        } else if (type == ParamConstant.CHANGYAN) {// 畅言
            if (StringUtils.isEmpty("changyan_app_id") || StringUtils.isEmpty("changyan_app_key")) {
                return false;
            }

        } else if (type == ParamConstant.GEETEST) {// 极验

            if (StringUtils.isEmpty("geetest_id") || StringUtils.isEmpty("geetest_key")) {
                return false;
            }
        }

        return true;
    }
}
