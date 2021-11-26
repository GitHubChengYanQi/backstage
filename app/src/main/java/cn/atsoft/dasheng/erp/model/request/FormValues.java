package cn.atsoft.dasheng.erp.model.request;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormValues {
    private Long field; //字段
    private DataValues dataValues;

    @Data
    public  static class DataValues {
        private String value;
        private List<ImgValues> imgValues ;
        @Data
        public  static class ImgValues {
            private Long id;
            private String url;
        }
    }


}
