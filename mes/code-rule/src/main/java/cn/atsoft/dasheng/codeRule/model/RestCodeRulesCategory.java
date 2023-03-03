package cn.atsoft.dasheng.codeRule.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 编码规则分类
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
@Data
public class RestCodeRulesCategory implements Serializable {
    //模块key
    private String module;
    //模块中文名
    private String name;
    //模块规则
    private List<RestCode> ruleList;


}
