package cn.atsoft.dasheng.production.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ImgParam {
    @NotNull
    private List<Long> imgs;
}
