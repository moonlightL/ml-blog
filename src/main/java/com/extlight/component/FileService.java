package com.extlight.component;

import com.extlight.web.exception.GlobalException;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
public class FileService {

    @Autowired
    private CommonMap commonMap;

    /**
     * 文件上传
     * @param inputStream
     * @param filename
     * @return
     */
    public Response upload(InputStream inputStream, String filename) throws GlobalException{

        // 有配置数据，但上传凭证为空
        if (!commonMap.containsKey("upToken")) {
            // 创建七牛云组件
            createQiniuComponent();
        }

        try {

            String upToken = (String) commonMap.get("upToken");
            UploadManager uploadManager = (UploadManager) commonMap.get("uploadManager");
            // 上传
            Response response = uploadManager.put(inputStream,filename,upToken,null, null);
            int retry = 0;
            while(response.needRetry() && retry < 3) {
                response = uploadManager.put(inputStream,filename,upToken,null, null);
                retry++;
            }

            return response;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("文件上传异常:",r.toString());
            throw new GlobalException(500, ex.toString());
        }
    }

    /**
     * 文件删除
     * @param key
     * @return
     */
    public Response delete(String key) throws GlobalException{

        // 有配置数据，但上传凭证为空
        if (!commonMap.containsKey("upToken")) {
            // 创建七牛云组件
            createQiniuComponent();
        }

        try {
            BucketManager bucketManager = (BucketManager) commonMap.get("bucketManager");
            String bucket = commonMap.get("qn_bucket").toString();
            Response response = bucketManager.delete(bucket, key);
            int retry = 0;
            while(response.needRetry() && retry < 3) {
                response = bucketManager.delete(bucket, key);
                retry++;
            }
            return response;
        } catch (QiniuException ex) {
            log.error("文件删除异常:",ex.toString());
            throw new GlobalException(500, ex.toString());
        }
    }

    /**
     * 创建七牛云相关组件
     */
    private void createQiniuComponent() {
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(commonMap.get("qn_accessKey").toString(), commonMap.get("qn_secretKey").toString());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        String upToken = auth.uploadToken(commonMap.get("qn_bucket").toString());
        commonMap.put("uploadManager",uploadManager);
        commonMap.put("bucketManager",bucketManager);
        commonMap.put("upToken",upToken);
    }
}

