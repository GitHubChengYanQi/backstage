package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.List;

@Data
public class RolePermission {
    private Long roleId;
    private List<CanDo> canDos;
}
