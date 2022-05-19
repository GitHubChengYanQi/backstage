package cn.atsoft.dasheng.form.pojo;

import cn.atsoft.dasheng.form.model.params.DocumentsPermissionsParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PermissionParam {
    private List<DocumentsPermissionsParam> params;
    @NotNull
    private String formType;
}
