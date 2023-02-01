package cn.atsoft.dasheng.coderule.model;

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

    private String module;

    private List<RestCode> ruleList;


}
