package cn.atsoft.dasheng.erp.pojo;

import cn.atsoft.dasheng.erp.enums.SearchTypeEnum;
import lombok.Data;

@Data
public class SearchObject {

    private String key;

    private String title;

    private SearchTypeEnum typeEnum;

    public Object objects;

}
