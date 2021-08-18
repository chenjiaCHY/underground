package com.ntschy.underground.zuul;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.ntschy.underground.dao.AuthorityDao;
import com.ntschy.underground.entity.base.PageInfo;
import com.ntschy.underground.entity.vo.RoleInfoVO;
import com.ntschy.underground.entity.vo.UserInfoVO;
import com.ntschy.underground.service.AuthorityService;
import com.ntschy.underground.utils.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * gis服务转发
 */

@Component
public class AccessFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Value("${filter.zuul}")
    private String authorityZuul;

    private final AuthorityService authorityService;

    @Resource
    private AuthorityDao authorityDao;

    public static final String LOGIN_USER_ID = "userID";

    public static final String LOGIN_NAME = "loginName";

    public AccessFilter(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    public String filterType() {
        // 前置过滤器
        return "pre";
    }

    @Override
    public int filterOrder() {
        // 优先级，数字越大，优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 是否执行该过滤器，true代表需要过滤
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String uri = request.getRequestURI().toString();

        if (!uri.startsWith("/arcgis")) {
            return null;
        }

        if (uri.startsWith("/arcgis/rest/services/Utilities/Geometry/GeometryServer")) {
            return null;
        }

        log.info("send {} request to {}", request.getMethod(), uri);

        // 获取传来的参数accessToken
        String token = request.getParameter("gis-t");

        if (!StringUtils.isBlank(token)) {
            DecodedJWT jwt = JwtUtil.getJWTData(token);
            String userID = jwt.getClaim(LOGIN_USER_ID).asString();
            String loginName = jwt.getClaim(LOGIN_NAME).asString();

            UserInfoVO userInfoVO = authorityService.getUserInfoVO(userID, loginName);

            if (userInfoVO == null) {
                log.error("url: {}, token为空", uri);
                // 过滤该请求，不往下级服务去转发请求，到此结束
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(403);
                ctx.setResponseBody("{\"result\":\"accessToken is empty!\"}");
                return null;
            } else {

                String roleID = Optional.ofNullable(userInfoVO.getRoleID()).orElse("");

                if (StringUtils.isBlank(roleID)) {
                    ctx.setSendZuulResponse(false);
                    ctx.setResponseStatusCode(403);
                    ctx.setResponseBody("{\"result\":\"no-authority\"}");
                    return null;
                }

                RoleInfoVO roleInfoVO = authorityDao.getSysRoleInfo(roleID);

                if (ObjectUtils.isEmpty(roleInfoVO)) {
                    ctx.setSendZuulResponse(false);
                    ctx.setResponseStatusCode(403);
                    ctx.setResponseBody("{\"result\":\"no-authority\"}");
                    return null;
                }

                List<PageInfo> pageInfoList = Optional.ofNullable(roleInfoVO.getPageInfos()).orElse(Collections.emptyList());

                if (CollectionUtils.isEmpty(pageInfoList)) {
                    ctx.setSendZuulResponse(false);
                    ctx.setResponseStatusCode(403);
                    ctx.setResponseBody("{\"result\":\"no-authority\"}");
                    return null;
                }

                String[] pageIds = authorityZuul.split(",");

                boolean result = true;
                for (String sting : pageIds) {
                    Integer pageId = Integer.valueOf(sting);
                    long count = pageInfoList.stream().filter(t -> t.getPageID() == pageId).count();
                    if (count > 0) {
                        result = false;
                        break;
                    }
                }

                if (result) {
                    ctx.setSendZuulResponse(false);
                    ctx.setResponseStatusCode(403);
                    ctx.setResponseBody("{\"result\":\"no-authority\"}");
                    return null;
                }

                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        ctx.addZuulRequestHeader(name, values);
                    }
                }

                return null;
            }
        } else {
            // 过滤该请求，不往下级服务去转发请求，到此结束
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
            ctx.setResponseBody("{\"result\":\"ccps-token is empty!\"}");
            log.info("send: {}, request to: {}, token为空，检验失败", request.getMethod(), request.getRequestURL().toString());
            return null;
        }
    }
}
