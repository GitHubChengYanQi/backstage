package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Bom;
import cn.atsoft.dasheng.erp.entity.BomDetail;
import cn.atsoft.dasheng.erp.mapper.BomMapper;
import cn.atsoft.dasheng.erp.model.params.BomDetailParam;
import cn.atsoft.dasheng.erp.model.params.BomParam;
import cn.atsoft.dasheng.erp.model.result.BomResult;
import cn.atsoft.dasheng.erp.service.BomDetailService;
import cn.atsoft.dasheng.erp.service.BomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BomServiceImpl extends ServiceImpl<BomMapper, Bom> implements BomService {

    @Autowired
    private BomDetailService bomDetailService;

    @Override
    public void add(BomParam bomParam) {
        if (ToolUtil.isNotEmpty(bomParam.getSkuId())) {
            Bom boms = new Bom();
            ToolUtil.copyProperties(bomParam, boms);
            boms.setBomId(null);
            this.save(boms);

            List<BomDetail> bomDetails = new ArrayList<>();


            for (BomDetailParam bomDetailParam : bomParam.getBomDetailParam()) {
                BomDetail bomDetail = new BomDetail();
                ToolUtil.copyProperties(bomDetailParam, bomDetail);
                bomDetail.setBomId(boms.getBomId());
                bomDetails.add(bomDetail);
            }
            bomDetailService.saveBatch(bomDetails);
        }
    }


    @Override
    public void delete(BomParam bomParam) {


    }

    @Override
    public void update(BomParam bomParam) {

    }

    @Override
    public BomResult findBySpec(BomParam bomParam) {
        return null;
    }

    @Override
    public List<BomResult> findListBySpec(BomParam bomParam) {
        return null;
    }

    @Override
    public PageInfo<BomResult> findPageBySpec(BomParam bomParam) {
        return null;
    }

    @Override
    public Integer countBySkuIdAndVersion(Long skuId, String version) {
        return this.baseMapper.countBySkuIdAndName(skuId,version);
    }

}
