package com.platform.common.utils;

import java.util.Random;

public class CodeUtils {

    public static String generateRandomCode(int index) {
        StringBuilder codeBuilder = new StringBuilder();

        // 创建一个Random对象
        Random random = new Random();

        for (int i = 0; i < index; i++) {
            int digit = random.nextInt(10); // 生成0到9之间的随机数
            // 将随机数转换为字符并添加到codeBuilder中
            char digitChar = (char)(digit + '0');
            codeBuilder.append(digitChar);
        }

        return codeBuilder.toString();
    }
}
