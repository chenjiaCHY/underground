package com.ntschy.underground.datasource.dynamic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源上下文，用户记录当前线程使用的数据源的key是什么， 以及记录所有注册成功的数据源的key的集合
 */
public class DynamicDataSourceContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    public static List<String> DataSourceIDs = new ArrayList<>();

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    public static String GetDataSourceRouteKey() {
        return HOLDER.get();
    }

    public static void SetDataSourceRouteKey(String dataSourceRouteKey) {
        logger.info("切换到{}数据源", dataSourceRouteKey);
        HOLDER.set(dataSourceRouteKey);
    }

    public static void RemoveDataSourceRouteKey() {
        HOLDER.remove();
    }

    /**
     * 判断指定的数据源ID当前是否存在
     * @param dataSourceID
     * @return
     */
    public static boolean ContainsDataSource(String dataSourceID) {
        return DataSourceIDs.contains(dataSourceID);
    }
}
