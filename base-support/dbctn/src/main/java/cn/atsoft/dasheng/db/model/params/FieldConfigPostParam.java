package cn.atsoft.dasheng.db.model.params;

import cn.atsoft.dasheng.portal.model.validator.BaseValidatingParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FieldConfigPostParam implements Serializable, BaseValidatingParam {

    private String tableName;

    private List<FieldConfigParam> fieldLists;
}
