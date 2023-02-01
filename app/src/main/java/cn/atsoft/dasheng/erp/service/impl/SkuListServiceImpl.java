package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.mapper.SkuListMapper;
import cn.atsoft.dasheng.erp.model.params.SkuListParam;
import cn.atsoft.dasheng.erp.model.result.SkuListResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuListService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkuListServiceImpl implements SkuListService {

    @Autowired
    private SkuListMapper skuListMapper;



    @Override
    public PageInfo listByKeyWord(SkuListParam skuListParam) {
        Page<SkuListResult> pageContext = getPageContext();
        IPage<SkuListResult> page = this.skuListMapper.customListBySkuView(pageContext, skuListParam);
        return PageFactory.createPageInfo(page);

    }

    private Page<SkuListResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("standard");
            add("skuName");
            add("price");
            add("brandName");
            add("spuId");
            add("spuName");
            add("categoryId");
            add("categoryName");
            add("bomNum");
            add("shipNum");
            add("stockNum");
            add("remarks");
            add("coding");
            add("modelCoding");
            add("specifications");
            add("partNo");
            add("createTime");
            add("stockNum");
            add("fileId");
            add("images");
            add("drawing");
            add("enclosure");
            add("nationalStandard");
            add("weight");
            add("skuSize");
            add("LEVEL");
            add("heatTreatment");
            add("packaging");
            add("viewFrame");
            add("model");
            add("modelCoding");
        }};
        return PageFactory.defaultPage(fields);
    }
}
