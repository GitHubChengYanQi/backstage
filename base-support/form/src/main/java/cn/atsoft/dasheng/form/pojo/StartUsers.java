package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.List;

@Data
public class StartUsers {

    private List<Depts> depts;
    private List<Users> users;
    private Boolean supervisor;

    @Data
    private class Depts {
        private String title;
        private String key;
    }

    @Data
    private class Users {
        private String title;
        private String key;
    }
}
