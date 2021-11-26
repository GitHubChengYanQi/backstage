package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.List;

@Data
public class StartUsers {

    private List<Depts> depts;
    private List<Users> users;
    private Boolean supervisor;

    @Data
    public class Depts {
        private String title;
        private String key;
    }

    @Data
    public class Users {
        private String title;
        private String key;
    }
}

