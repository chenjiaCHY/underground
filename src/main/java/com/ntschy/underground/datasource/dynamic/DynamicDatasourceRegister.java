package com.ntschy.underground.datasource.dynamic;

import com.ntschy.underground.utils.Utils;
import com.zaxxer.hikari.HikariDataSource;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源的注册
 * 实现 ImportBeanDefinitionRegistrar 实现数据源注册
 * 实现 EnvironmentAware 用于读取application.yml配置
 */
@Component
public class DynamicDatasourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDatasourceRegister.class);

    private Environment env;

    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

    static {
        aliases.addAliases("url", new String[] {"jdbc-url"});
        aliases.addAliases("username", new String[] {"user"});
    }

    private Map<String, DataSource> customDataSources = new HashMap<>();

    private Binder binder;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 默认的主数据源
        Map defaultDataSourceProperties = binder.bind("spring.datasource.master", Map.class).get();
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword("NTSchy123456");
        defaultDataSourceProperties.put("username", encryptor.decrypt(defaultDataSourceProperties.get("username").toString()));
        defaultDataSourceProperties.put("password", encryptor.decrypt(defaultDataSourceProperties.get("password").toString()));

        String typeStr = env.getProperty("spring.datasource.master.type");
        Class<? extends DataSource> clazz = getDataSourceType(typeStr);
        DataSource defaultDatasource = bind(clazz, defaultDataSourceProperties);
        DynamicDataSourceContextHolder.DataSourceIDs.add("master");
        logger.info("注册默认数据源master成功");

        try {
            // 其他数据源
            List<Map> configs = binder.bind("spring.datasource.cluster", Bindable.listOf(Map.class)).get();

            if (configs != null && configs.size() > 0) {
                for (int i = 0; i < configs.size(); i ++) {
                    Map clusterDatasourceProperties = configs.get(i);

                    clusterDatasourceProperties.put("username", encryptor.decrypt(clusterDatasourceProperties.get("username").toString()));
                    clusterDatasourceProperties.put("password", encryptor.decrypt(clusterDatasourceProperties.get("password").toString()));

                    List list = new ArrayList(clusterDatasourceProperties.values());
                    for (Object o : list) {
                        System.out.println(o);
                    }
                    String clusterTypeStr = (String) clusterDatasourceProperties.get("type");
                    Class clusterClazz = null;
                    if (!Utils.IsBlank(clusterTypeStr)) {
                        clusterClazz = getDataSourceType(clusterTypeStr);
                    } else {
                        clusterClazz = HikariDataSource.class;
                    }

                    DataSource clusterDatasource = bind(clusterClazz, clusterDatasourceProperties);
                    String key = (String) clusterDatasourceProperties.get("key");
                    customDataSources.put(key, clusterDatasource);
                    DynamicDataSourceContextHolder.DataSourceIDs.add(key);

                    System.out.println("SIO+++" + customDataSources);

                    logger.info("注册数据源{}成功", key);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        GenericBeanDefinition define = new GenericBeanDefinition();
        define.setBeanClass(DynamicRoutingDataSource.class);
        MutablePropertyValues mpv = define.getPropertyValues();
        mpv.add("defaultTargetDataSource", defaultDatasource);
        mpv.add("targetDataSources", customDataSources);
        registry.registerBeanDefinition("datasource", define);

        logger.info("注册数据源成功，一共注册{}个数据源", customDataSources.keySet().size() + 1);
    }

    private Class<? extends DataSource> getDataSourceType(String typeStr) {
        Class<? extends DataSource> type;
        try {
            if (!Utils.IsBlank(typeStr)) {
                type = (Class<? extends DataSource>) Class.forName(typeStr);
            } else {
                type = HikariDataSource.class;
            }

            return type;
        } catch (Exception e) {
            throw new IllegalArgumentException("无法通过此字符串反射获取class对象：" + typeStr);
        }
    }

    /**
     * 绑定参数，以下三个方法都是参考DataSourceBuilder的bind方法实现的，目的是尽量保证我们自己添加的数据源构造过程与springboot保持一致
     * @param result
     * @param properties
     */
    private void bind(DataSource result, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        // 将参数绑定到对象
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        // 通过类型绑定参数并获得实例对象
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get();
    }

    /**
     *
     * @param clazz
     * @param sourcePath 参数路径，对应配置文件中的值，如： spring.datasource
     * @param <T>
     * @return
     */
    private <T extends DataSource> T bind(Class<T> clazz, String sourcePath) {
        Map properties = binder.bind(sourcePath, Map.class).get();
        return bind(clazz, properties);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        binder = Binder.get(this.env);
    }
}
