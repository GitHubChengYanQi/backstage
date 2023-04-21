package cn.atsoft.dasheng.core.datascope;

import java.util.List;

/**
 * 数据范围
 */
public class DataScope {

    /**
     * 限制范围的字段名称
     */
    private String scopeName = "deptId";

    /**
     * 具体的数据范围
     */
    private List<Long> deptIds;

    private  Long tenantId;

    public DataScope() {
    }

    public DataScope(List<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public DataScope(List<Long> deptIds,Long tenantId) {
        this.deptIds = deptIds;
        this.tenantId = tenantId;
    }

    public DataScope(String scopeName, List<Long> deptIds) {
        this.scopeName = scopeName;
        this.deptIds = deptIds;
    }

    public List<Long> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
