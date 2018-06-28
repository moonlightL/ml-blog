package com.extlight.web.controller.admin;

import com.extlight.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author MoonlightL
 * @Title: DatabaseControll
 * @ProjectName ml-blog
 * @Description:
 * @date 2018/6/27 0027 下午 07:40
 */
@Controller
@RequestMapping("/admin/database")
@Slf4j
public class DatabaseController {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;


    @RequestMapping("/exportSQL")
    public void exportSQL(HttpServletRequest request, HttpServletResponse response) {

        // 获取数据库名称
        String tmp = url.substring(0,url.indexOf("?"));
        String dbName = tmp.substring(url.lastIndexOf("/") + 1);

        // 判断系统
        String systemName = System.getProperty("os.name");
        String dumpCmd;
        if (systemName.toUpperCase().indexOf("WINDOWS") > -1) {
            dumpCmd = "mysqldump.exe";
        } else {
            dumpCmd = "mysqldump";
        }

        // 执行命令获取 sql 内容
        String sqlStr;
        if (StringUtils.isEmpty(password)) {
            sqlStr = this.execCommand(dumpCmd + " -u" + username + " --databases " + dbName);
        } else {
            sqlStr = this.execCommand(dumpCmd + " -u" + username + " -p" + password + " --databases " + dbName);
        }

        if (StringUtils.isEmpty(sqlStr)) {
            log.info("导出 SQL 文件失败【sql 内容为空】");
            return;
        }

        String tmpDir = System.getProperty("java.io.tmpdir");
        String date = DateUtil.formateToStr(new Date(),"yyyyMMddHHmmss");
        String filePath = tmpDir + File.separator + "ml-blog-" + date + ".sql";

        ZipOutputStream zipOut = null;
        try {
            byte[] data = sqlStr.getBytes("UTF-8");
            try (OutputStream output = new FileOutputStream(new File(filePath))) {
                // 将 sql 内容保存到本地
                IOUtils.write(data, output);
            }

            response.setContentType("application/zip");
            String fileName = "ml-blog-sql-" + date + ".zip";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // 读取 sql 文件并压缩
            zipOut = new ZipOutputStream(response.getOutputStream());
            // 写出
            this.zipFile(zipOut,filePath);
            zipOut.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipOut != null) {
                    zipOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String execCommand(String command) {
        InputStream in = null;
        Process process = null;
        String result = null;
        try {
            process = Runtime.getRuntime().exec(command);
            in = process.getInputStream();
            result = IOUtils.toString(in, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private void zipFile(ZipOutputStream zipOut, String filePath) {
        FileInputStream fis = null;
        try {
            zipOut.putNextEntry(new ZipEntry(filePath));
            fis = new FileInputStream(new File(filePath));
            byte[] buffer = new byte[1024];
            int r;
            while ((r = fis.read(buffer)) != -1) {
                zipOut.write(buffer, 0, r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
