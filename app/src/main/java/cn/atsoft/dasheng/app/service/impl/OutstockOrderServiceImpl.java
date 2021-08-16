package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.OutstockOrder;
import cn.atsoft.dasheng.app.mapper.OutstockOrderMapper;
import cn.atsoft.dasheng.app.model.params.OutstockOrderParam;
import cn.atsoft.dasheng.app.model.result.OutstockOrderResult;
import  cn.atsoft.dasheng.app.service.OutstockOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 出库单 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-16
 */
@Service
public class OutstockOrderServiceImpl extends ServiceImpl<OutstockOrderMapper, OutstockOrder> implements OutstockOrderService {

    @Override
    public Long add(OutstockOrderParam param){
        OutstockOrder entity = getEntity(param);
        this.save(entity);
        return entity.getOutstockOrderId();
    }

    @Override
    public void delete(OutstockOrderParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OutstockOrderParam param){
        OutstockOrder oldEntity = getOldEntity(param);
        OutstockOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OutstockOrderResult findBySpec(OutstockOrderParam param){
        return null;
    }

    @Override
    public List<OutstockOrderResult> findListBySpec(OutstockOrderParam param){
        return null;
    }

    @Override
    public PageInfo<OutstockOrderResult> findPageBySpec(OutstockOrderParam param){
        Page<OutstockOrderResult> pageContext = getPageContext();
        IPage<OutstockOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutstockOrderParam param){
        return param.getOutstockOrderId();
    }

    private Page<OutstockOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OutstockOrder getOldEntity(OutstockOrderParam param) {
        return this.getById(getKey(param));
    }

    private OutstockOrder getEntity(OutstockOrderParam param) {
        OutstockOrder entity = new OutstockOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
