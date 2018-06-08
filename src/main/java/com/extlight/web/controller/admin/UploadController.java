package com.extlight.web.controller.admin;

import com.extlight.aspect.SysLog;
import com.extlight.common.constant.ParamConstant;
import com.extlight.common.utils.ParamUtil;
import com.extlight.common.vo.FileResult;
import com.extlight.common.vo.Result;
import com.extlight.component.CommonMap;
import com.extlight.component.FileService;
import com.extlight.web.exception.GlobalException;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.model.DefaultPutRet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件上传相关
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class UploadController {

    @Autowired
    private FileService fileService;
    @Autowired
    private CommonMap commonMap;


    /**
     * markdown 编辑器专用文件上传
     * @param request
     * @param response
     * @param attach
     * @return
     */
    @SysLog("mdUploadfile 文件上传")
    @PostMapping("/mdUploadfile")
    @ResponseBody
    public FileResult mdUploadfile(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "editormd-image-file", required = false) MultipartFile attach) throws GlobalException{
        try {

            if (!ParamUtil.checkParameter(ParamConstant.QINIU)) {
                throw new GlobalException(500,"未配置七牛云参数");
            }

            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/json");

            Response resp = this.fileService.upload(attach.getInputStream(), attach.getOriginalFilename());
            if (!resp.isOK()) {
                log.error("文件上传失败 -> url:{},Response:{}","/mdUploadfile",resp);
                throw new GlobalException(500,"文件上传失败");
            }
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(resp.bodyString(), DefaultPutRet.class);

            String resultKey = putRet.key == null ? putRet.hash : putRet.key;

            //下面response返回的json格式是editor.md所限制的，规范输出就OK
            return FileResult.success(commonMap.get("qn_domain").toString() + "/" + resultKey);

        } catch (Exception e) {
            throw new GlobalException(500,e.getMessage());
        }
    }

    /**
     * 分类界面专用文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @SysLog("uploadfile 文件上传")
    @PostMapping("/uploadfile")
    @ResponseBody
    public Result uploadfile(MultipartFile file) throws GlobalException {
        try {

            if (!ParamUtil.checkParameter(ParamConstant.QINIU)) {
               throw new GlobalException(500,"未配置七牛云参数");
            }

            Response response = this.fileService.upload(file.getInputStream(), file.getOriginalFilename());
            if (!response.isOK()) {
                log.error("文件上传失败 -> url:{},Response:{}","/uploadfile",response);
                throw new GlobalException(500,"文件上传失败");
            }
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            String resultKey = putRet.key == null ? putRet.hash : putRet.key;
            return Result.success(commonMap.get("qn_domain").toString() + "/" + resultKey);

        } catch (GlobalException e) {
            throw new GlobalException(500,e.getMessage());
        } catch (Exception e) {
            throw new GlobalException(500,e.toString());
        }
    }

}