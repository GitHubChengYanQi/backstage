package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.Values;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.entity.SkuList;
import cn.atsoft.dasheng.erp.mapper.SkuListMapper;
import cn.atsoft.dasheng.erp.model.params.SkuJson;
import cn.atsoft.dasheng.erp.model.params.SkuListParam;
import cn.atsoft.dasheng.erp.model.result.SkuListResult;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.erp.service.SkuListService;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkuListServiceImpl extends ServiceImpl<SkuListMapper, SkuList> implements SkuListService {

//    @Autowired
//    private SkuListMapper skuListMapper;

    @Autowired
    private MediaService mediaService;
    @Autowired
    private SkuService skuService;


    @Autowired
    private AttributeValuesService attributeValuesService;
    @Autowired
    private ItemAttributeService itemAttributeService;
    @Override
    public PageInfo pageListByKeyWord(SkuListParam skuListParam) {
        Page<SkuListResult> pageContext = getPageContext();
        IPage<SkuListResult> page = this.baseMapper.customPageListBySkuView(pageContext, skuListParam);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }
    @Override
    public List<SkuListResult> listByKeyWord(SkuListParam skuListParam) {
        List<SkuListResult> list = this.baseMapper.customListBySkuView(skuListParam);
        this.format(list);
        return list;
    }

    public void format(List<SkuListResult> dataList){
        List<Long> attributeIds = new ArrayList<>();

        for (SkuListResult skuListResult : dataList) {
            JSONArray jsonArray = JSONUtil.parseArray(skuListResult.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            for (AttributeValues valuesRequest : valuesRequests) {
                attributeIds.add(valuesRequest.getAttributeId());
            }
        }
        List<ItemAttribute> itemAttributes = itemAttributeService.lambdaQuery().list();

        List<AttributeValues> attributeValues = attributeIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery()
                .in(AttributeValues::getAttributeId, attributeIds)
                .list();
        for (SkuListResult skuListResult : dataList) {
            JSONArray jsonArray = JSONUtil.parseArray(skuListResult.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            List<SkuJson> list = new ArrayList<>();
            for (AttributeValues valuesRequest : valuesRequests) {
                if (ToolUtil.isNotEmpty(valuesRequest.getAttributeId()) && ToolUtil.isNotEmpty(valuesRequest.getAttributeValuesId())) {
                    SkuJson skuJson = new SkuJson();
                    for (ItemAttribute itemAttribute : itemAttributes) {
                        if (itemAttribute.getAttributeId().equals(valuesRequest.getAttributeId())) {
                            Attribute attribute = new Attribute();
                            attribute.setAttributeId(itemAttribute.getAttributeId().toString());
                            attribute.setAttribute(itemAttribute.getAttribute());
                            skuJson.setAttribute(attribute);
                        }
                    }
                    for (AttributeValues attributeValue : attributeValues) {
                        if (valuesRequest.getAttributeValuesId().equals(attributeValue.getAttributeValuesId())) {
                            Values values = new Values();
                            values.setAttributeValuesId(valuesRequest.getAttributeValuesId().toString());
                            values.setAttributeValues(attributeValue.getAttributeValues());
                            skuJson.setValues(values);
                        }
                    }
                    list.add(skuJson);
                }
                skuListResult.setSkuJsons(list);
            }
        }
    }
    @Override
    public List<SkuListResult> resultByIds(List<Long> ids) {
        if (ToolUtil.isEmpty(ids) || ids.size()== 0){
            return new ArrayList<>();
        }
        List<SkuListResult> skuListResults = BeanUtil.copyToList(this.listByIds(ids), SkuListResult.class);

        format(skuListResults);


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
