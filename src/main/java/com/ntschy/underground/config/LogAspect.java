package com.ntschy.underground.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ntschy.underground.entity.base.OperationLog;
import com.ntschy.underground.entity.base.TokenInfo;
import com.ntschy.underground.handler.OperateLogHandler;
import com.ntschy.underground.service.AuthorityService;
import com.ntschy.underground.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private WhiteList whiteList;

    private final AuthorityService authorityService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public LogAspect(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Pointcut("execution(* com.ntschy.underground.controller..*(..))")
    public void pointcut() {

    }
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    ThreadLocal<Long> endTime = new ThreadLocal<>();
    private static final String TRACE_ID = "traceId";

    /**
     * 调用方法前记录日志
     * @param joinPoint
     * @throws Throwable
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        if (StringUtils.isBlank(MDC.get(TRACE_ID))) {
            String traceId = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "-"
                    + ((int)((Math.random() * 9 + 1) * 100000));
            MDC.put(TRACE_ID, traceId);
        }
        ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String url = requestAttr.getRequest().getRequestURL().toString();

        List<String> whiteLists = Optional.ofNullable(whiteList.getUrlList()).orElse(Collections.emptyList());

        boolean result = false;
        if (StringUtils.isNotBlank(url)) {
            if (!CollectionUtils.isEmpty(whiteLists)) {
                long count = whiteLists.stream().filter(t -> url.contains(t)).count();
                if (count > 0) {
                    result = true;
                }
            }
        }

        String methodName = joinPoint.getSignature().getName();
        String requParam = getParams(joinPoint);
        String ip = getIpAddr(requestAttr.getRequest());
        log.info("请求时间:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime.get()));
        log.info("请求URL:{}", url);
        log.info("请求方法:{}", methodName);
        log.info("请求参数:{}", requParam);
        log.info("请求IP:{}", ip);

        OperationLog operationLog = new OperationLog();
        operationLog.setOperateTime(Utils.GetCurrentDateTime());
        operationLog.setRequestURI(url);
        operationLog.setOperateRequParam(requParam);
        operationLog.setOperateMethod(methodName);
        operationLog.setOperateIp(ip);
        operationLog.setId(Utils.GenerateUUID(32));
        OperateLogHandler.set(operationLog);

        if (!result) {
            String token = requestAttr.getRequest().getHeader("token");
            if (StringUtils.isNotBlank(token)) {
                TokenInfo tokenInfo = Utils.getTokenInfo(token);
                operationLog.setOperateUser(tokenInfo.getLoginName());
            } else {
                operationLog.setOperateUser("");
            }
        } else {
            operationLog.setOperateUser("");
        }
    }

    /**
     * 方法完成后记录日志
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "pointcut()")
    public void doAfterReturning(Object ret) throws Throwable {
        endTime.set(System.currentTimeMillis());
        int retLength = 0;
        if (ret != null) {
            retLength = ret.toString().length();
        }
        if (retLength > 100) {
            retLength = 100;
        }
        if (ret != null) {
            log.info("返回数据:" + ret.toString().substring(0, retLength));
        }
        log.info("结束时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime.get()));
        log.info("用时:" + (endTime.get() - startTime.get()));
    }

    /**
     * 记录异常日志
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowable(JoinPoint joinPoint, Throwable e) {
        if (StringUtils.isBlank(MDC.get(TRACE_ID))) {
            String traceId = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime.get()) + "-"
                    + ((int)((Math.random() * 9 + 1) * 100000));
            MDC.put(TRACE_ID, traceId);
        }
        ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String url = requestAttr.getRequest().getRequestURL().toString();
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String exName = e.getClass().getName();
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        log.error("切面发生了异常:");
        log.error("异常发生时间:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        log.error("异常名称:{}", e.getClass().getName());
        log.error("异常信息:{}", stackTraceElements);

        log.info("请求时间:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime.get()));
        log.info("请求URL:{}", url);
        log.info("请求方法:{}", methodName);
        log.info("请求参数:{}", getParams(joinPoint));
        log.info("请求IP:{}", getIpAddr(requestAttr.getRequest()));
    }

    /**
     * 获得IP地址
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.error("获取IP异常:{}", e.getMessage());
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }

        return ipAddress;
    }

    /**
     * 获得详细参数
     * @param joinPoint
     * @return
     */
    private String getParams(JoinPoint joinPoint) {
        String params = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            for (int i = 0; i < joinPoint.getArgs().length; i ++) {
                Object arg = joinPoint.getArgs()[i];
                if ((arg instanceof HttpServletResponse) || (arg instanceof  HttpServletRequest)
                || (arg instanceof MultipartFile) || (arg instanceof MultipartFile[])) {
                    continue;
                }
                try {
                    params += JSONObject.toJSONString(joinPoint.getArgs()[i]);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return params;
    }
}
