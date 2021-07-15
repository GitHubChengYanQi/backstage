package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Order;
import cn.atsoft.dasheng.app.mapper.OrderMapper;
import cn.atsoft.dasheng.app.model.params.OrderParam;
import cn.atsoft.dasheng.app.model.result.OrderResult;
import  cn.atsoft.dasheng.app.service.OrderService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 发货表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-15
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public void add(OrderParam param){
        Order entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OrderParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OrderParam param){
        Order oldEntity = getOldEntity(param);
        Order newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OrderResult findBySpec(OrderParam param){
        return null;
    }

    @Override
    public List<OrderResult> findListBySpec(OrderParam param){
        return null;
    }

    @Override
    public PageInfo<OrderResult> findPageBySpec(OrderParam param){
        Page<OrderResult> pageContext = getPageContext();
        IPage<OrderResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OrderParam param){
        return param.getOrderId();
    }

    private Page<OrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Order getOldEntity(OrderParam param) {
        return this.getById(getKey(param));
    }

    private Order getEntity(OrderParam param) {
        Order entity = new Order();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
