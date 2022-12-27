package cn.atsoft.dasheng.form.model.result;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

@Data
public class TaskViewResult {
    private int status;
    private int number;
    private String type;
    private Long createUser;
    private UserResult userResult;
}
