package cn.atsoft.dasheng.form.model.result;

import lombok.Data;

@Data
public class StartUsers {

    private String label;
    private Long value;
    private Depts depts;
    private Users users;

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
