package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StockLogDetail;
import cn.atsoft.dasheng.erp.mapper.StockLogDetailMapper;
import cn.atsoft.dasheng.erp.model.params.StockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.StockLogDetailResult;
import  cn.atsoft.dasheng.erp.service.StockLogDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 库存操作记录子表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
@Service
public class StockLogDetailServiceImpl extends ServiceImpl<StockLogDetailMapper, StockLogDetail> implements StockLogDetailService {

    @Override
    public void add(StockLogDetailParam param){
        StockLogDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(StockLogDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(StockLogDetailParam param){
        StockLogDetail oldEntity = getOldEntity(param);
        StockLogDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StockLogDetailResult findBySpec(StockLogDetailParam param){
        return null;
    }

    @Override
    public List<StockLogDetailResult> findListBySpec(StockLogDetailParam param){
        return null;
    }

    @Override
    public PageInfo<StockLogDetailResult> findPageBySpec(StockLogDetailParam param){
        Page<StockLogDetailResult> pageContext = getPageContext();
        IPage<StockLogDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StockLogDetailParam param){
        return param.getStockLogDetailId();
    }

    private Page<StockLogDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StockLogDetail getOldEntity(StockLogDetailParam param) {
        return this.getById(getKey(param));
    }

    private StockLogDetail getEntity(StockLogDetailParam param) {
        StockLogDetail entity = new StockLogDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
