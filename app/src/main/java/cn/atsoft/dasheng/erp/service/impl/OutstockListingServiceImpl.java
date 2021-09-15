package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.mapper.OutstockListingMapper;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import  cn.atsoft.dasheng.erp.service.OutstockListingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 出库清单 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-09-15
 */
@Service
public class OutstockListingServiceImpl extends ServiceImpl<OutstockListingMapper, OutstockListing> implements OutstockListingService {

    @Override
    public void add(OutstockListingParam param){
        OutstockListing entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OutstockListingParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OutstockListingParam param){
        OutstockListing oldEntity = getOldEntity(param);
        OutstockListing newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OutstockListingResult findBySpec(OutstockListingParam param){
        return null;
    }

    @Override
    public List<OutstockListingResult> findListBySpec(OutstockListingParam param){
        return null;
    }

    @Override
    public PageInfo<OutstockListingResult> findPageBySpec(OutstockListingParam param){
        Page<OutstockListingResult> pageContext = getPageContext();
        IPage<OutstockListingResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void addList(List<ApplyDetailsParam> applyDetails) {
//        for (ApplyDetailsParam applyDetail : applyDetails) {
//            ApplyDetails details = new ApplyDetails();
//            int number = Math.toIntExact(applyDetail.getNumber());
//            for (int i = 0; i < number; i++) {
//                details.setItemId(applyDetail.getItemId());
//                details.setBrandId(details.getBrandId());
//                details.setNumber(1L);
//            }
//        }
    }

    private Serializable getKey(OutstockListingParam param){
        return param.getOutstockListingId();
    }

    private Page<OutstockListingResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OutstockListing getOldEntity(OutstockListingParam param) {
        return this.getById(getKey(param));
    }

    private OutstockListing getEntity(OutstockListingParam param) {
        OutstockListing entity = new OutstockListing();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
