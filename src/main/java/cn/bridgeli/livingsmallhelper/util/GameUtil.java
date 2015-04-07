package cn.bridgeli.livingsmallhelper.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameUtil {

    public static boolean verifyNo(String number) {
        boolean result = false;
        Pattern pattern = Pattern.compile("[0-9]{4}");
        Matcher matcher = pattern.matcher(number);
        if (matcher.matches()) {
            result = true;
        }
        return result;
    }

    public static boolean verifyRepeat(String number) {
        boolean result = false;
        if (null != number && !"".equals(number)) {
            for (int i = 0; i < number.length(); i++) {
                for (int j = 0; j < i; j++) {
                    if (number.charAt(i) == number.charAt(j)) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static String generateRandNo() {
        StringBuffer stringBuffer = new StringBuffer();
        String scope = "0123456789";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int no = random.nextInt(scope.length());
            stringBuffer.append(scope.charAt(no));
            scope = scope.replace(String.valueOf(scope.charAt(no)), "");
        }
        return stringBuffer.toString();
    }

    public static String quessResult(String gameAnswer, String content) {
        int rightA = 0;
        int rightB = 0;
        for (int i = 0; i < 4; i++) {
            if (gameAnswer.charAt(i) == content.charAt(i)) {
                rightA++;
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != j) {
                    if (gameAnswer.charAt(j) == content.charAt(i)) {
                        rightB++;
                    }
                }
            }
        }
        return String.format("%sA%sB", rightA, rightB);
    }

}
