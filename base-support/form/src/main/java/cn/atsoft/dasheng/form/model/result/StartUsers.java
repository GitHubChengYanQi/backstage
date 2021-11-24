package cn.atsoft.dasheng.form.model.result;

import lombok.Data;

import java.util.List;

@Data
public class StartUsers {

    private String label;
    private Long value;
    private List<Depts> depts;
    private List<Users> users;

    @Data
    private class Depts {
        private String title;
        private Long key;
    }

    @Data
    private class Users {
        private String title;
        private Long key;
    }
}
