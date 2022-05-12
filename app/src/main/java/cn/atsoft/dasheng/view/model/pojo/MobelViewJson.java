package cn.atsoft.dasheng.view.model.pojo;

import cn.atsoft.dasheng.app.model.params.Attribute;
import lombok.Data;

@Data
public class MobelViewJson implements Comparable<MobelViewJson>  {

    private Integer sort;
    private String code;
    private String name;
    @Override
    public int compareTo(MobelViewJson o) {
        return this.sort - o.getSort();
    }
}
