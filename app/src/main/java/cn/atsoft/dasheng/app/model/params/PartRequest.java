package cn.atsoft.dasheng.app.model.params;

import lombok.Data;

import java.util.List;

@Data
public class PartRequest {
    private List<PartsParam> parts;
    private Long pid;
}
