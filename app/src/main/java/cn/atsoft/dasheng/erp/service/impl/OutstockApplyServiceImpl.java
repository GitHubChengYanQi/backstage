package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.OutstockOrder;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.OutstockApply;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.mapper.OutstockApplyMapper;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.OutstockApplyResult;
import cn.atsoft.dasheng.erp.service.ApplyDetailsService;
import cn.atsoft.dasheng.erp.service.OutstockApplyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 出库申请 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-09-14
 */
@Service
public class OutstockApplyServiceImpl extends ServiceImpl<OutstockApplyMapper, OutstockApply> implements OutstockApplyService {
    @Autowired
    private OutstockOrderService outstockOrderService;
    @Autowired
    private OutstockService outstockService;
    @Autowired
    private ApplyDetailsService applyDetailsService;
    @Autowired
    private OutstockListingService outstockListingService;

    @Override
    public void add(OutstockApplyParam param) {
        OutstockApply entity = getEntity(param);
        this.save(entity);
        List<ApplyDetails> applyDetailsList = new ArrayList<>();
        if (ToolUtil.isNotEmpty(param.getApplyDetails())) {
            for (ApplyDetailsParam applyDetailsParam : param.getApplyDetails()) {
                ApplyDetails applyDetails = new ApplyDetails();
                applyDetails.setOutstockApplyId(entity.getOutstockApplyId());
                applyDetails.setBrandId(applyDetailsParam.getBrandId());
                applyDetails.setItemId(applyDetailsParam.getItemId());
                applyDetails.setNumber(applyDetailsParam.getNumber());
                applyDetailsList.add(applyDetails);
            }
            if (ToolUtil.isNotEmpty(applyDetailsList)) {
                applyDetailsService.saveBatch(applyDetailsList);
            }
        }

    }

    @Override
    public void delete(OutstockApplyParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(OutstockApplyParam param) {

        OutstockApply oldEntity = getOldEntity(param);
        OutstockApply newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        if (newEntity.getApplyState().equals(2)) {
            //添加发货单
            OutstockOrderParam outstockOrderParam = new OutstockOrderParam();
            outstockOrderParam.setOutstockApplyId(newEntity.getOutstockApplyId());
            OutstockOrder outstockOrder = outstockOrderService.add(outstockOrderParam);
            List<OutstockListing> outstockListings = new ArrayList<>();
            for (ApplyDetailsParam applyDetail : param.getApplyDetails()) {
                OutstockListing outstockListing = new OutstockListing();
                outstockListing.setBrandId(applyDetail.getBrandId());
                outstockListing.setItemId(applyDetail.getItemId());
                outstockListing.setNumber(applyDetail.getNumber());
                outstockListing.setOutstockOrderId(outstockOrder.getOutstockOrderId());
                outstockListings.add(outstockListing);
            }
            if (ToolUtil.isNotEmpty(outstockListings)) {
                outstockListingService.saveBatch(outstockListings);
            }

        }


    }

    @Override
    public OutstockApplyResult findBySpec(OutstockApplyParam param) {
        return null;
    }

    @Override
    public List<OutstockApplyResult> findListBySpec(OutstockApplyParam param) {
        return null;
    }

    @Override
    public PageInfo<OutstockApplyResult> findPageBySpec(OutstockApplyParam param) {
        Page<OutstockApplyResult> pageContext = getPageContext();
        IPage<OutstockApplyResult> page = this.baseMapper.customPageList(pageContext, param);


        for (OutstockApplyResult record : page.getRecords()) {
            QueryWrapper<ApplyDetails> applyDetailsQueryWrapper = new QueryWrapper<>();
            applyDetailsQueryWrapper.in("outstock_apply_id", record.getOutstockApplyId());
            List<ApplyDetails> list = applyDetailsService.list(applyDetailsQueryWrapper);
            record.setApplyDetails(list);
        }

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutstockApplyParam param) {
        return param.getOutstockApplyId();
    }

    private Page<OutstockApplyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OutstockApply getOldEntity(OutstockApplyParam param) {
        return this.getById(getKey(param));
    }

    private OutstockApply getEntity(OutstockApplyParam param) {
        OutstockApply entity = new OutstockApply();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}