package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

@Data
public class UserList {
    public UserList(Long userId, String name, String avatar) {
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
    }

    private Long userId;
    private String name;
    private String avatar;
}
