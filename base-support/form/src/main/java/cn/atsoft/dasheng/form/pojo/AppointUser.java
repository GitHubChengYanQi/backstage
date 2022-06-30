package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AppointUser {

    private String title;

    private String key;

    private Integer auditStatus;   //审核状态

}
