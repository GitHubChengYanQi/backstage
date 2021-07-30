package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpOrder;
import cn.atsoft.dasheng.app.mapper.ErpOrderMapper;
import cn.atsoft.dasheng.app.model.params.ErpOrderParam;
import cn.atsoft.dasheng.app.model.result.ErpOrderResult;
import cn.atsoft.dasheng.app.service.ErpOrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
@Service
public class ErpOrderServiceImpl extends ServiceImpl<ErpOrderMapper, ErpOrder> implements ErpOrderService {

    @Override
    public void add(ErpOrderParam param) {
        ErpOrder entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ErpOrderParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ErpOrderParam param) {
        ErpOrder oldEntity = getOldEntity(param);
        ErpOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ErpOrderResult findBySpec(ErpOrderParam param) {
        return null;
    }

    @Override
    public List<ErpOrderResult> findListBySpec(ErpOrderParam param) {
        return null;
    }

    @Override
    public PageInfo<ErpOrderResult> findPageBySpec(ErpOrderParam param) {
        Page<ErpOrderResult> pageContext = getPageContext();
        IPage<ErpOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ErpOrderParam param) {
        return param.getOrderId();
    }

    private Page<ErpOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ErpOrder getOldEntity(ErpOrderParam param) {
        return this.getById(getKey(param));
    }

    private ErpOrder getEntity(ErpOrderParam param) {
        ErpOrder entity = new ErpOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
