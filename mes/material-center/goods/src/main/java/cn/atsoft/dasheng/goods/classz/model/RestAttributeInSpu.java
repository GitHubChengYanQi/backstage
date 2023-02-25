package cn.atsoft.dasheng.goods.classz.model;

import cn.atsoft.dasheng.core.util.ToolUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class RestAttributeInSpu implements Comparable<RestAttributeInSpu>{
    private Long k_s;
    private String k;
    private List<RestAttributeValueInSpu> v;
    private Long sort;
    @Override
    public int compareTo(RestAttributeInSpu o) {
        if (ToolUtil.isNotEmpty(o.sort)){
            return (int)(this.sort - o.sort);
        }
        return 0;
    }


}
