package com.ntschy.underground.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ntschy.underground.entity.base.TokenInfo;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Utils<T> {

    public static final Object YYYYMMDDHH24MMSSSHORT = "";

    public static final String YYYY = "yyyy";

    public static boolean IsBlank(String str) {
        if (str == null) {
            return true;
        }

        if (str.trim().equals("")) {
            return true;
        }

        return false;
    }

    public static boolean checkPass(String pass) {
        if (pass.matches(".*[a-zA-Z]{1,}.*") && pass.matches(".*\\d{1,}.*")) {
            return true;
        }
        return false;
    }

    public static final String YYYYMMDDHH24MMSS = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYYMMDDHH24MMSS);

    public static final String YYYYMMDD = "yyyy-MM-dd";

    public static String ConvertDateToString(Calendar cal, String pattern) {
        // 将时间对象格式化为字符串
        Date now = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String strDate = format.format(now);
        return strDate;
    }

    public static String ConvertDateToString(Date date, String pattern) {
        // 将时间对象格式化为字符串
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String strDate = format.format(date);
        return strDate;
    }

    public static String GetCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(YYYYMMDDHH24MMSS);
        String strDate = format.format(now);
        return strDate;
    }

    public static String GetCurrentDateDelayHours(long hours) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime delayTime = localDateTime.plusHours(hours);
        return delayTime.format(dateTimeFormatter);
    }

    public static String GetCurrentDate() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(YYYYMMDD);
        String strDate = format.format(now);
        return strDate;
    }

    public static String ByteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i ++) {
            strDigest += ByteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    public static String ByteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    /**
     * 生成UUID
     * @param len
     * @return
     */
    public static String GenerateUUID(Integer len) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        StringBuffer str = new StringBuffer();

        if (len == null || len <= 0) {
            return str.toString();
        }

        for (int i = 0; i < len; i ++) {
            str.append(uuid.charAt(i));
        }

        return str.toString();
    }

    public static int getSubCount(String str, String key) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();
            count ++;
        }
        return count;
    }

    public static void main(String[] args) {
        String url = "http://192.168.35.45:8088/authority/getInfo";
        String urlList = StringUtils.substringAfterLast(url, ":");
        System.out.println(StringUtils.substringAfter(urlList, "/"));
    }

    public static TokenInfo getTokenInfo(String token) {
        DecodedJWT jwt = JwtUtil.getJWTData(token);
        TokenInfo ti = new TokenInfo();
        ti.setUserID(jwt.getClaim("userID").asString());
        ti.setLoginName(jwt.getClaim("loginName").asString());
        return ti;
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (null == request) {
            return null;
        }

        String proxs[] = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "x-real-ip"};

        String ip = null;

        for (String proxy : proxs) {
            ip = request.getHeader(proxy);
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                continue;
            } else {
                break;
            }
        }
        if (StringUtils.isBlank(ip)) {
            return request.getRemoteAddr();
        }
        return ip;
    }

    public static String getNewAutoNum(Integer Num) {
        return String.format("%03d", Num);
    }

    public static String getNewAutoNumB(Integer Num) {
        return String.format("%04d", Num);
    }

    public static String getNewAutoNumA(Integer Num) {
        return String.format("%02d", Num);
    }
}
