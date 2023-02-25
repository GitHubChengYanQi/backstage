package cn.atsoft.dasheng.codeRule.model;

import lombok.Data;

@Data
public class RestCode {

    /**
     * {cateGoryId}
     * {cateGoryCoDe}
     */
    private String label;

    /**
     * 分类ID
     * 分类编码
     */
    private String value;

}
