package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.List;

@Data
public class StartUsers {

    private List<Depts> depts;
    private List<Users> users;

    @Data
    public class Depts {
        private String title;
        private String key;
        private List<Positions> positions;

        @Data
        private class Positions {
            private String label;
            private String value;
        }
    }

    @Data
    public class Users {
        private String title;
        private String key;
    }
}

