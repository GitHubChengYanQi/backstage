package cn.atsoft.dasheng.purchase.pojo;

import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ThemeAndOrigin {

        private String source;
        private Long sourceId;
        private List<ThemeAndOrigin> parent;
        private Ret ret;
        @Data
        public static class Ret{
                private UserResult createUser;
                private Date createTime;
                private String from;
                private Long fromId;
                private String coding;
                private String title;
        }



}
