package cn.atsoft.dasheng.erp.model.result;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class AttributeInSpu {
    private Long k_s;
    private String k;
    private List<AttributeValueInSpu> v;

}
