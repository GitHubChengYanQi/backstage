package cn.atsoft.dasheng.bom.service.impl;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.entity.RestBom;
import cn.atsoft.dasheng.bom.entity.RestBomDetail;
import cn.atsoft.dasheng.bom.mapper.RestBomMapper;
import cn.atsoft.dasheng.bom.model.params.RestBomDetailParam;
import cn.atsoft.dasheng.bom.model.params.RestBomParam;
import cn.atsoft.dasheng.bom.model.result.RestBomResult;
import cn.atsoft.dasheng.bom.service.RestBomDetailService;
import cn.atsoft.dasheng.bom.service.RestBomService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RestBomServiceImpl extends ServiceImpl<RestBomMapper, RestBom> implements RestBomService {

    @Autowired
    private RestBomDetailService bomDetailService;

    @Override
    public void add(RestBomParam bomParam) {
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
            List<Long> tmp = ToolUtil.isEmpty(restBom.getChildrens())?new ArrayList<>():Arrays.stream(restBom.getChildrens().split(",")).map(i->Long.parseLong(i.trim())).collect(Collectors.toList());
            childrens.addAll(tmp);
        }
        boms.setChildren(StringUtils.join(children,","));
        boms.setChildrens(StringUtils.join(childrens,","));
        this.updateById(boms);
    }

    public List<RestBom> getByBomIds(List<Long> bomIds) {
        return this.listByIds(bomIds);
    }
    @Override
    public void cycleJudge(Long skuId, List<RestBom> bomList){
        if (bomList.stream().anyMatch(i->i.getSkuId().equals(skuId))){
            throw new ServiceException(500,"子件中不得出现主件");
        }
        List<String> childrensStrList = bomList.stream().map(RestBom::getChildrens).collect(Collectors.toList());
        List<Long> childrensIds = new ArrayList<>();
        for (String childrensStr : childrensStrList) {
            childrensIds.addAll(ToolUtil.isEmpty(childrensStr)?new ArrayList<>():Arrays.stream(childrensStr.split(",")).map(i->Long.parseLong(i.trim())).collect(Collectors.toList()));
        }
        List<RestBom> childrens = this.getByBomIds(childrensIds);
        if (childrens.stream().anyMatch(i->i.getSkuId().equals(skuId))){
            throw new ServiceException(500,"不可循环添加bom");
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
    public PageInfo<RestBomResult> findPageBySpec(RestBomParam bomParam) {
        return null;
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


}
