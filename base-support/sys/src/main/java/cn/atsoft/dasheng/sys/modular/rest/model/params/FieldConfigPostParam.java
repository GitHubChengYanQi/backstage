package cn.atsoft.dasheng.sys.modular.rest.model.params;

import cn.atsoft.dasheng.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FieldConfigPostParam implements Serializable, BaseValidatingParam {

    private String tableName;

    private List<FieldConfigParam> fieldLists;
}
