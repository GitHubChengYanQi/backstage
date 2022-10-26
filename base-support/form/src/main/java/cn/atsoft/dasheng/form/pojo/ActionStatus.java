package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

@Data
public class ActionStatus {

    private Long actionId;

    private Integer status;

    private String formType;

    private String action;

    private String actionName;

    private Boolean checked = false;
}
