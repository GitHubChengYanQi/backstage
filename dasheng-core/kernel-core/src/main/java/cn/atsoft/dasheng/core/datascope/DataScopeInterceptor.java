package cn.atsoft.dasheng.core.datascope;


import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.auth.context.LoginUserHolder;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据范围的拦截器
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        //获取数据库类型
        Connection connection = (Connection) invocation.getArgs()[0];
        DbType dbType = JdbcUtils.getDbType(connection.getMetaData().getURL());

        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();

        //查找参数中包含DataScope类型的参数
        DataScope dataScope = findDataScopeObject(parameterObject);

        if (dataScope == null) {
            return invocation.proceed();
        } else {
            String scopeName = dataScope.getScopeName();
            List<Long> deptIds = dataScope.getDeptIds();
            String join = CollectionUtil.join(deptIds, ",");
            //TODO 增加租户id

            //如果是pgsql，进行一下大小写敏感处理
            if (dbType != null && dbType.equals(DbType.POSTGRE_SQL)) {
                originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope.\"" + scopeName + "\" in (" + join + ")";
                if (ToolUtil.isNotEmpty(dataScope.getTenantId())){
                    originalSql+=" and temp_data_scope.tenantId = "+dataScope.getTenantId();
                }
            } else {
                originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";
                if (ToolUtil.isNotEmpty(dataScope.getTenantId())){
                    originalSql+=" and temp_data_scope.tenantId = "+dataScope.getTenantId();
                }
            }

            metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
            return invocation.proceed();
        }
    }

    /**
     * 查找参数是否包括DataScope对象
     */
    public DataScope findDataScopeObject(Object parameterObj) {
        if (parameterObj instanceof DataScope) {
            return (DataScope) parameterObj;
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof DataScope) {
                    return (DataScope) val;
                }
            }
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
