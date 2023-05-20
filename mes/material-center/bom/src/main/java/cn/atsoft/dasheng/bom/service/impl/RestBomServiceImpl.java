package cn.atsoft.dasheng.bom.service.impl;

import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.entity.RestBom;
import cn.atsoft.dasheng.bom.entity.RestBomDetail;
import cn.atsoft.dasheng.bom.mapper.RestBomMapper;
import cn.atsoft.dasheng.bom.model.params.RestBomDetailParam;
import cn.atsoft.dasheng.bom.model.params.RestBomParam;
import cn.atsoft.dasheng.bom.model.result.RestBomDetailResult;
import cn.atsoft.dasheng.bom.model.result.RestBomResult;
import cn.atsoft.dasheng.bom.service.RestBomDetailService;
import cn.atsoft.dasheng.bom.service.RestBomService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.sku.entity.RestSku;
import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RestBomServiceImpl extends ServiceImpl<RestBomMapper, RestBom> implements RestBomService {

    @Autowired
    private RestBomDetailService bomDetailService;

    @Autowired
    private RestSkuService restSkuService;

    @Override
    @Transactional
    public void add(RestBomParam bomParam) {
        this.checkData(bomParam);
        RestBom boms = new RestBom();
        ToolUtil.copyProperties(bomParam, boms);
        boms.setBomId(null);
        this.save(boms);

        List<RestBomDetail> bomDetails = new ArrayList<>();


        List<Long> bomIds = new ArrayList<>();
        for (RestBomDetailParam bomDetailParam : bomParam.getBomDetailParam()) {
            RestBomDetail bomDetail = new RestBomDetail();
            ToolUtil.copyProperties(bomDetailParam, bomDetail);
            bomDetail.setBomId(boms.getBomId());
            bomDetails.add(bomDetail);
            bomIds.add(bomDetailParam.getVersionBomId());
        }
        bomDetailService.saveBatch(bomDetails);

        List<Long> children = new ArrayList<>();
        List<Long> childrens = new ArrayList<>();

        childrens.add(boms.getBomId());
        List<RestBom> restBomList = this.getByBomIds(bomIds);
        for (RestBom restBom : restBomList) {
            children.add(restBom.getBomId());
            List<Long> tmp = ToolUtil.isEmpty(restBom.getChildrens()) ? new ArrayList<>() : Arrays.stream(restBom.getChildrens().split(",")).map(i -> Long.parseLong(i.trim())).collect(Collectors.toList());
            childrens.addAll(tmp);
        }
        boms.setChildren(StringUtils.join(children, ","));
        boms.setChildrens(StringUtils.join(childrens, ","));
        this.updateById(boms);
    }

    public List<RestBom> getByBomIds(List<Long> bomIds) {
        if (ToolUtil.isEmpty(bomIds) || bomIds.size() == 0) {
            return new ArrayList<>();
        }
        return this.listByIds(bomIds);
    }

    @Override
    public void cycleJudge(Long skuId, List<RestBom> bomList) {
        if (bomList.stream().anyMatch(i -> i.getSkuId().equals(skuId))) {
            throw new ServiceException(500, "子件中不得出现主件");
        }
        List<String> childrensStrList = bomList.stream().map(RestBom::getChildrens).collect(Collectors.toList());
        List<Long> childrensIds = new ArrayList<>();
        for (String childrensStr : childrensStrList) {
            childrensIds.addAll(ToolUtil.isEmpty(childrensStr) ? new ArrayList<>() : Arrays.stream(childrensStr.split(",")).map(i -> Long.parseLong(i.trim())).collect(Collectors.toList()));
        }
        List<RestBom> childrens = this.getByBomIds(childrensIds);
        if (childrens.stream().anyMatch(i -> i.getSkuId().equals(skuId))) {
            throw new ServiceException(500, "不可循环添加bom");
        }


    }

    @Override
    public void delete(RestBomParam bomParam) {


    }

    @Override
    public void update(RestBomParam bomParam) {

    }


    @Override
    public RestBomResult findBySpec(RestBomParam bomParam) {
        return null;
    }

    @Override
    public List<RestBomResult> findListBySpec(RestBomParam bomParam) {
        return null;
    }

    @Override
    public PageInfo<RestBomResult> findPageBySpec(RestBomParam bomParam, DataScope dataScope) {
        Page<RestBomResult> pageContext = getPageContext();
        Page<RestBomResult> page = this.baseMapper.customPageList(pageContext, bomParam,dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void format(List<RestBomResult> dataList) {
        List<Long> skuIds = dataList.stream().map(RestBomResult::getSkuId).distinct().collect(Collectors.toList());
        List<RestSkuResult> restSkuResults = restSkuService.formatSkuResult(skuIds);
        for (RestBomResult bom : dataList) {
            for (RestSkuResult sku : restSkuResults) {
                if (bom.getSkuId().equals(sku.getSkuId())) {
                    bom.setSkuResult(sku);
                    break;
                }
            }
        }
    }


    @Override
    public Integer countBySkuIdAndVersion(Long skuId, String version) {
        return this.count(new QueryWrapper<RestBom>() {{
            eq("sku_id", skuId);
            eq("version", version);
        }});
    }

    @Override
    public List<RestBom> getBySkuIds(List<Long> skuIds) {
        skuIds.add(0L);
        return this.baseMapper.getBySkuIds(skuIds);
    }

    public void checkData(RestBomParam bomParam) {
        if (ToolUtil.isEmpty(bomParam.getVersion())) {
            throw new ServiceException(500, "版本号不能为空");
        }

        if (ToolUtil.isEmpty(bomParam.getSkuId())) {
            throw new ServiceException(500, "请选择物料");
        }

        RestSku restSkus = restSkuService.getById(bomParam.getSkuId());
        if (ToolUtil.isEmpty(restSkus)) {
            throw new ServiceException(500, "该物料不存在");
        }

        Integer count = this.countBySkuIdAndVersion(bomParam.getSkuId(), bomParam.getVersion());
        if (count > 0) {
            throw new ServiceException(500, "版本号不能重复");
        }

        List<Long> skuIds = new ArrayList<>();
        List<RestBomDetailParam> restBomDetailParams = bomParam.getBomDetailParam();
        for (RestBomDetailParam bomDetailParam : restBomDetailParams) {
            skuIds.add(bomDetailParam.getSkuId());
        }

        List<RestSku> skuList = restSkuService.getByIds(skuIds);

        if (skuList.size() != restBomDetailParams.size()) {
            throw new ServiceException(500, "物料详情错误");
        }

//        int i=0;
        List<RestBom> bomList = this.getBySkuIds(skuIds);
        //拦截循环添加
        this.cycleJudge(bomParam.getSkuId(), bomList);

        for (RestBomDetailParam bomDetailParam : restBomDetailParams) {
            //如果传入versionBomId 但是versionBomId 并没有匹配到正确数据
            if (bomList.stream().anyMatch(e -> e.getSkuId().equals(bomDetailParam.getSkuId())) && (ToolUtil.isNotEmpty(bomDetailParam.getVersionBomId()) && bomList.stream().noneMatch(e -> e.getBomId().equals(bomDetailParam.getVersionBomId())))) {
                throw new ServiceException(500, "子件bom版本信息错误");
            }
            //如果没传入 versionBomId 但是子件物料存在bom 请指定bom版本
            if (bomList.stream().anyMatch(e -> e.getSkuId().equals(bomDetailParam.getSkuId())) && ToolUtil.isEmpty(bomDetailParam.getVersionBomId())) {
                throw new ServiceException(500, "请指定子件bom版本");
            }
        }
    }

    private Page<RestBomResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    @Override
    public List<RestBomResult> getByBomId(Long bomId, Integer number) {
        List<RestBomResult> result = new ArrayList<>();
        loopGetByBomId(bomId, 0L, number, result);
        List<Long> skuIds = result.stream().map(RestBomResult::getSkuId).distinct().collect(Collectors.toList());
        List<RestSkuResult> skuSimpleResults = restSkuService.formatSkuResult(skuIds);
        for (RestBomResult inStockByBom : result) {
            for (RestSkuResult skuSimpleResult : skuSimpleResults) {
                if (inStockByBom.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    inStockByBom.setSkuResult(skuSimpleResult);
                    break;
                }
            }
        }
        return result;
    }

    public void loopGetByBomId(Long bomId, Long parentId, Integer number, List<RestBomResult> result) {
        RestBom bom = this.getById(bomId);

        List<RestBomDetail> details = bomDetailService.lambdaQuery().eq(RestBomDetail::getBomId, bom.getBomId()).eq(RestBomDetail::getDisplay, 1).list();

        List<RestBomDetail> havePartDetailList = new ArrayList<>();


        List<Long> childrenIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(bom.getChildren())) {
            childrenIds = Arrays.stream(bom.getChildren().split(",")).map(Long::valueOf).collect(Collectors.toList());
        }
        List<RestBom> childrenList = childrenIds.size() == 0 ? new ArrayList<>() : this.listByIds(childrenIds);
//        Map<Long, Integer> childrenAndNumber = new HashMap<>();
        for (RestBom parts : childrenList) {
            for (RestBomDetail detail : details) {
                if (parts.getBomId().equals(detail.getVersionBomId())) {
                    havePartDetailList.add(detail);
                    loopGetByBomId(parts.getBomId(), bom.getBomId(), (number * detail.getNumber()), result);

                }
            }
        }

        for (RestBomDetail detail : details) {
            detail.setNumber(detail.getNumber() * number);
        }
        details.removeIf(i -> ToolUtil.isNotEmpty(i.getVersionBomId()));
//        details.removeAll(havePartDetailList);

        List<RestBomDetailResult> RestBomDetailResults = BeanUtil.copyToList(details, RestBomDetailResult.class);

        result.add(new RestBomResult() {{
            setBomId(bom.getBomId());
            setNumber(number);
            setSkuId(bom.getSkuId());
            setVersion(bom.getVersion());
            setDetailResults(RestBomDetailResults);
            setParentId(parentId);
        }});

    }


}
