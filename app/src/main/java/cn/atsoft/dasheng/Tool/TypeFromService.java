package cn.atsoft.dasheng.Tool;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface TypeFromService {
    void autoAudit(Long activitiTaskId, String type);
}
