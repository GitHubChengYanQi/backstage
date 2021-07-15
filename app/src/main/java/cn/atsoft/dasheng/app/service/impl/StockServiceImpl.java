package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Stock;
import cn.atsoft.dasheng.app.mapper.StockMapper;
import cn.atsoft.dasheng.app.model.params.StockParam;
import cn.atsoft.dasheng.app.model.result.StockResult;
import  cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 仓库总表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Override
    public void add(StockParam param){
        Stock entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(StockParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(StockParam param){
        Stock oldEntity = getOldEntity(param);
        Stock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StockResult findBySpec(StockParam param){
        return null;
    }

    @Override
    public List<StockResult> findListBySpec(StockParam param){
        return null;
    }

    @Override
    public PageInfo<StockResult> findPageBySpec(StockParam param){
        Page<StockResult> pageContext = getPageContext();
        IPage<StockResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(StockParam param){
        return param.getStockId();
    }

    private Page<StockResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Stock getOldEntity(StockParam param) {
        return this.getById(getKey(param));
    }

    private Stock getEntity(StockParam param) {
        Stock entity = new Stock();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
