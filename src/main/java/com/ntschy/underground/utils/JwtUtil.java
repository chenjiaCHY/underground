package com.ntschy.underground.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ntschy.underground.entity.base.Result;
import com.ntschy.underground.entity.base.TokenInfo;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

public class JwtUtil {

    /**
     * 过期时间为一天
     */
    public static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    public static final int EXPIRE_HOUR = 24;

    /**
     * token 私钥
     */
    private static final String TOKEN_SECRET = "joijsdfjlsjfljfljl15135313135";

    /**
     * 生成签名，15分钟后过期
     * @param username
     * @param userID
     * @return
     */
    public static String sign(String username, String userID) {
        // 过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        // 私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        // 设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        // 附带username和userID生成签名
        return JWT.create().withHeader(header).withClaim("loginName", username)
                .withClaim("userID", userID).withExpiresAt(date).sign(algorithm);
    }

    public static TokenInfo getTokenInfo(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userID = jwt.getClaim("userID").asString();
            String adminUserID = jwt.getClaim("loginName").asString();
            return new TokenInfo(userID, adminUserID, null);
        } catch (IllegalArgumentException | JWTVerificationException e) {
            return null;
        }
    }

    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println(jwt.getClaim("userID").asString());
            System.out.println(jwt.getClaim("loginName").asString());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public static DecodedJWT getJWTData(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public static Result getUserIDByToken(HttpServletRequest request) {

        String token = request.getHeader("token");

        if (StringUtils.isBlank(token)) {
            return new Result(false, "token 不能为空");
        }

        DecodedJWT jwt = JwtUtil.getJWTData(token);
        if (jwt == null) {
            return new Result(false, "无效的token");
        }

        String userID = Optional.ofNullable(jwt.getClaim("userID").asString()).orElse("");
        if (StringUtils.isBlank(userID)) {
            return new Result(false, "userID 不能为空");
        }

        return new Result(true, userID);
    }
}
