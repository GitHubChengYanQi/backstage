package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data

public class AuditRule {
    private QualityRules qualityRules;
}
