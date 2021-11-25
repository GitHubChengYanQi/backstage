package cn.atsoft.dasheng.form.model.result;

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
    public class Users {
        private String title;
        private Long key;
    }
}

