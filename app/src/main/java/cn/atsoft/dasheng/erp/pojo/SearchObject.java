package cn.atsoft.dasheng.erp.pojo;

import cn.atsoft.dasheng.erp.SearchTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class SearchObject {

    private String key;

    private String title;

    private SearchTypeEnum typeEnum;

    private Object objects;
}
