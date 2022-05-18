package cn.atsoft.dasheng.form.pojo;

import cn.atsoft.dasheng.form.model.params.DocumentsPermissionsParam;
import lombok.Data;

import java.util.List;

@Data
public class PermissionParam {
    private List<DocumentsPermissionsParam> params;
}
