package cn.atsoft.dasheng.sys.modular.system.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserParam implements Serializable, BaseValidatingParam {

    private String userName;

    private Long userId;

    private Long deptId;

    private List<Long> userIds;
}
