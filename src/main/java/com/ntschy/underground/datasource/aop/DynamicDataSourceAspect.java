package com.ntschy.underground.datasource.aop;

import com.ntschy.underground.datasource.annotation.DataSource;
import com.ntschy.underground.datasource.dynamic.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DynamicDataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint joinPoint, DataSource ds) throws Throwable {
        String dsID = ds.value();
        if (DynamicDataSourceContextHolder.ContainsDataSource(dsID)) {
            DynamicDataSourceContextHolder.SetDataSourceRouteKey(dsID);
            logger.debug("Use DataSource: {} > {}", dsID, joinPoint.getSignature());
        } else {
            logger.info("数据源{}不存在，使用默认数据源 > {}", dsID, joinPoint.getSignature());
            DynamicDataSourceContextHolder.SetDataSourceRouteKey(dsID);
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, DataSource ds) {
        logger.debug("Revert DataSource : " + ds.value() + " > " + point.getSignature());
        DynamicDataSourceContextHolder.RemoveDataSourceRouteKey();
    }
}
