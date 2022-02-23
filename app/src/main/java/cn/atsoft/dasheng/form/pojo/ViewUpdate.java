package cn.atsoft.dasheng.form.pojo;

import cn.atsoft.dasheng.sys.modular.system.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class ViewUpdate {
    private Date updateTime;
    private User updateUser;
}
