package com.campus.libraryborrowbackend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
        // 需要传递 "recognize" 参数作为第一个参数
        String[] cmd = {pythonPath, scriptPath, "recognize", imgPath};
        Process process = null;

        try {
            process = new ProcessBuilder(cmd)
                    .redirectErrorStream(true)
                    .start();

            // 一次性读取所有输出，同时做日志打印 + 结果提取
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8")
            );
            List<String> allLines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                allLines.add(line);
                // 实时打印到SpringBoot控制台
                System.out.println("[Python日志] " + line);
            }

            process.waitFor();

            // 最后一行作为Python返回的条码结果（你main.py逻辑：仅print最终码）
            if (!allLines.isEmpty()) {
                return allLines.get(allLines.size() - 1).trim();
            }
            return "";

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