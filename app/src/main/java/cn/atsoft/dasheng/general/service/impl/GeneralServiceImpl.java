package cn.atsoft.dasheng.general.service.impl;

import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.general.mapper.GeneralMapper;

import cn.atsoft.dasheng.general.model.result.BomListResult;
import cn.atsoft.dasheng.general.model.result.ClassListResult;
import cn.atsoft.dasheng.general.model.result.GeneralResult;
import cn.atsoft.dasheng.general.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GeneralServiceImpl implements GeneralService {
    @Autowired
    private GeneralMapper generalMapper;

    @Override
    public List<ClassListResult> listByspuClassName(String keyWord) {
        List<ClassListResult> results = this.generalMapper.customList(keyWord);
        return results;
    }

    @Override
    public List<BomListResult> listBySkuName(String keyWord) {
        List<BomListResult> results = this.generalMapper.customListBySkuName(keyWord);
        return results;
    }
}
