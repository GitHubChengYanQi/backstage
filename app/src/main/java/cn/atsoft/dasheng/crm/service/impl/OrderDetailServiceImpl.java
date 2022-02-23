package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.OrderDetail;
import cn.atsoft.dasheng.crm.mapper.OrderDetailMapper;
import cn.atsoft.dasheng.crm.model.params.OrderDetailParam;
import cn.atsoft.dasheng.crm.model.result.OrderDetailResult;
import cn.atsoft.dasheng.crm.service.OrderDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-23
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Override
    public void add(OrderDetailParam param) {
        OrderDetail entity = getEntity(param);
        this.save(entity);
    }

    /**
     * 批量添加
     *
     * @param orderId
     * @param params
     */

    @Override
    public void addList(Long orderId, List<OrderDetailParam> params) {
        List<OrderDetail> details = new ArrayList<>();
        for (OrderDetailParam param : params) {
            OrderDetail orderDetail = new OrderDetail();
            ToolUtil.copyProperties(param, orderDetail);
            orderDetail.setOrderId(orderId);
            details.add(orderDetail);
        }
        this.saveBatch(details);
    }

    @Override
    public void delete(OrderDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(OrderDetailParam param) {
        OrderDetail oldEntity = getOldEntity(param);
        OrderDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OrderDetailResult findBySpec(OrderDetailParam param) {
        return null;
    }

    @Override
    public List<OrderDetailResult> findListBySpec(OrderDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<OrderDetailResult> findPageBySpec(OrderDetailParam param) {
        Page<OrderDetailResult> pageContext = getPageContext();
        IPage<OrderDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OrderDetailParam param) {
        return param.getDetailId();
    }

    private Page<OrderDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OrderDetail getOldEntity(OrderDetailParam param) {
        return this.getById(getKey(param));
    }

    private OrderDetail getEntity(OrderDetailParam param) {
        OrderDetail entity = new OrderDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
