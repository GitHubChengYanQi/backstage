package cn.atsoft.dasheng.erp.model.result;

import cn.atsoft.dasheng.app.model.params.Attribute;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class AttributeInSpu implements Comparable<AttributeInSpu>{
    private Long k_s;
    private String k;
    private List<AttributeValueInSpu> v;
    private Long sort;
    @Override
    public int compareTo(AttributeInSpu o) {
        return (int)(this.sort - o.sort);
    }


}
