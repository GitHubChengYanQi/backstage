package cn.stylefeng.guns.dbcontainer.core.context;

import cn.stylefeng.guns.dbcontainer.core.collector.SqlSessionFactoryCreator;
import cn.stylefeng.guns.dbcontainer.core.exception.DataSourceInitException;
import cn.stylefeng.roses.core.config.properties.DruidProperties;
import cn.stylefeng.roses.core.util.SpringContextHolder;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.stylefeng.guns.dbcontainer.core.context.DataSourceContext.MASTER_DATASOURCE_NAME;

/**
 * mybatis的sqlSessionFactory的上下文容器(单例)
 *
 * @author fengshuonan
 * @date 2019-06-12-13:37
 */
public class SqlSessionFactoryContext {

    private static Map<Object, SqlSessionFactory> sqlSessionFactories = new ConcurrentHashMap<>();

    /**
     * 添加sqlSessionFactory
     *
     * @author fengshuonan
     * @Date 2019-06-12 15:28
     */
    public static void addSqlSessionFactory(String name, SqlSessionFactory sqlSessionFactory) {
        sqlSessionFactories.put(name, sqlSessionFactory);
    }

    /**
     * 获取所有的sqlSessionFactory
     *
     * @author fengshuonan
     * @Date 2019-06-12 13:49
     */
    public static Map<Object, SqlSessionFactory> getSqlSessionFactorys() {
        return sqlSessionFactories;
    }

    /**
     * 初始化数据库中的数据源的SqlSessionFactory
     *
     * @author fengshuonan
     * @Date 2019-06-15 19:51
     */
    public static void initBaseSqlSessionFactory(SqlSessionFactoryCreator sqlSessionFactoryCreator) {

        //初始化数据源容器
        try {
            DruidProperties druidProperties = SpringContextHolder.getBean(DruidProperties.class);
            DataSourceContext.initDataSource(druidProperties);
        } catch (Exception e) {
            throw new DataSourceInitException(DataSourceInitException.ExEnum.INIT_DATA_SOURCE_ERROR);
        }

        //获取数据库的数据源
        Map<String, DataSource> dataSources = DataSourceContext.getDataSources();

        //创建数据库中数据源的sqlSessionFactory
        for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
            String dbName = entry.getKey();
            DataSource dataSource = entry.getValue();

            //如果是主数据源，跳过，否则会冲突
            if (MASTER_DATASOURCE_NAME.equals(dbName)) {
                continue;
            } else {
                SqlSessionFactory sqlSessionFactory = sqlSessionFactoryCreator.createSqlSessionFactory(dataSource);
                SqlSessionFactoryContext.addSqlSessionFactory(dbName, sqlSessionFactory);
            }
        }

    }

}
