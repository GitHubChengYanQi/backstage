package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.appBase.model.result.MediaUrlResult;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.SkuList;
import cn.atsoft.dasheng.erp.mapper.SkuListMapper;
import cn.atsoft.dasheng.erp.model.params.SkuListParam;
import cn.atsoft.dasheng.erp.model.result.SkuListResult;
import cn.atsoft.dasheng.erp.service.SkuListService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkuListServiceImpl extends ServiceImpl<SkuListMapper, SkuList> implements SkuListService {

//    @Autowired
//    private SkuListMapper skuListMapper;

    @Autowired
    private MediaService mediaService;
    @Autowired
    private SkuService skuService;


    @Override
    public PageInfo listByKeyWord(SkuListParam skuListParam) {
        Page<SkuListResult> pageContext = getPageContext();
        IPage<SkuListResult> page = this.baseMapper.customListBySkuView(pageContext, skuListParam);
        return PageFactory.createPageInfo(page);

    }


    @Override
    public List<SkuListResult> resultByIds(List<Long> ids) {
        List<SkuListResult> skuListResults = BeanUtil.copyToList(this.listByIds(ids), SkuListResult.class);

//        for (SkuListResult skuListResult : skuListResults) {
//            if (ToolUtil.isNotEmpty(skuListResult.getImages())) {
//              skuListResult.setImgResults( skuService.strToMediaResults(skuListResult.getImages()));
//            }
//        }


        return skuListResults;

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
