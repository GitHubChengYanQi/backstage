package cn.atsoft.dasheng.sys.modular.rest.model.params;

import cn.atsoft.dasheng.portal.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class DeptParam implements Serializable, BaseValidatingParam {

    private Long deptId;

    private String condition;
}
