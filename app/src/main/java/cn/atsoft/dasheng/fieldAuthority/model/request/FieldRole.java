package cn.atsoft.dasheng.fieldAuthority.model.request;

import lombok.Data;

import java.util.List;

@Data
public class FieldRole {
    private String fieldName;
    private List<RoleAuthority> roleAuthorities;


    @Data
    public static class RoleAuthority{
        private Long roleId;
        private Integer chmod;
    }
}
