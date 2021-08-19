package cn.atsoft.dasheng.model.api.model;

import cn.atsoft.dasheng.model.request.AbstractBaseRequest;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.atsoft.dasheng.model.resource.ResourceDefinition;
import lombok.Data;

import java.util.Map;

/**
 * 汇报资源请求
 *
 * @author fengshuonan
 * @Date 2019/5/13 20:51
 */
@Data
public class ReportResourceReq extends AbstractBaseRequest {

    /**
     * app编码
     */
    private String appCode;

    /**
     * 资源集合
     */
    private Map<String, Map<String, ResourceDefinition>> resourceDefinitions;

    public ReportResourceReq(String appCode, Map<String, Map<String, ResourceDefinition>> resourceDefinitions) {
        this.appCode = appCode;
        this.resourceDefinitions = resourceDefinitions;
    }

    @Override
    public String checkParam() {
        if (StrUtil.isEmpty(appCode)) {
            return "请求应用编码为空！";
        }
        if (CollectionUtil.isEmpty(resourceDefinitions)) {
            return "请求资源为空！";
        }
        return null;
    }
}
