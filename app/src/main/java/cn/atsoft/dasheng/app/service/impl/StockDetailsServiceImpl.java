package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.StockDetails;
import cn.atsoft.dasheng.app.mapper.StockDetailsMapper;
import cn.atsoft.dasheng.app.model.params.StockDetailsParam;
import cn.atsoft.dasheng.app.model.result.StockDetailsResult;
import  cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 仓库物品明细表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Service
public class StockDetailsServiceImpl extends ServiceImpl<StockDetailsMapper, StockDetails> implements StockDetailsService {

    @Override
    public Long add(StockDetailsParam param){
        StockDetails entity = getEntity(param);
        this.save(entity);
        return entity.getStockItemId();
    }

    @Override
    public void delete(StockDetailsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(StockDetailsParam param){
        StockDetails oldEntity = getOldEntity(param);
        StockDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StockDetailsResult findBySpec(StockDetailsParam param){
        return null;
    }

    @Override
    public List<StockDetailsResult> findListBySpec(StockDetailsParam param){
        List<StockDetailsResult> stockDetailsResults = this.baseMapper.customList(param);
        return stockDetailsResults;
    }

    @Override
    public PageInfo<StockDetailsResult> findPageBySpec(StockDetailsParam param){
        Page<StockDetailsResult> pageContext = getPageContext();
        IPage<StockDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StockDetailsParam param){
        return param.getStockItemId();
    }

    private Page<StockDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private StockDetails getOldEntity(StockDetailsParam param) {
        return this.getById(getKey(param));
    }

    private StockDetails getEntity(StockDetailsParam param) {
        StockDetails entity = new StockDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
