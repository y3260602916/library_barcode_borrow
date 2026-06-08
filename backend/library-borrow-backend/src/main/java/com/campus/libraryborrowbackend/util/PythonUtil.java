package com.campus.libraryborrowbackend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class PythonUtil {

    @Value("${python.python-path}")
    private String pythonPath;

    @Value("${python.script-path}")
    private String scriptPath;

    /**
     * 调用Python条码识别脚本
     * @param imgPath 图片绝对路径
     * @return 条码字符串 / null
     */
    public String getBarcode(String imgPath) {
        // 拼接执行命令：python main.py 图片路径
        String[] cmd = {pythonPath, scriptPath, imgPath};
        Process process = null;

        try {
            process = new ProcessBuilder(cmd)
                    .redirectErrorStream(true) // 合并错误流，方便调试
                    .start();

            // 读取Python输出结果
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8")
            );
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // 等待脚本执行完毕
            process.waitFor();
            return result.toString().trim();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }
}