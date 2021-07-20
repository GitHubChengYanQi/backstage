package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.OrderBranch;
import cn.atsoft.dasheng.app.mapper.OrderBranchMapper;
import cn.atsoft.dasheng.app.model.params.OrderBranchParam;
import cn.atsoft.dasheng.app.model.result.OrderBranchResult;
import  cn.atsoft.dasheng.app.service.OrderBranchService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单分表 服务实现类
 * </p>
 *
 * @author ta
 * @since 2021-07-20
 */
@Service
public class OrderBranchServiceImpl extends ServiceImpl<OrderBranchMapper, OrderBranch> implements OrderBranchService {

    @Override
    public void add(OrderBranchParam param){
        OrderBranch entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OrderBranchParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OrderBranchParam param){
        OrderBranch oldEntity = getOldEntity(param);
        OrderBranch newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OrderBranchResult findBySpec(OrderBranchParam param){
        return null;
    }

    @Override
    public List<OrderBranchResult> findListBySpec(OrderBranchParam param){
        return null;
    }

    @Override
    public PageInfo<OrderBranchResult> findPageBySpec(OrderBranchParam param){
        Page<OrderBranchResult> pageContext = getPageContext();
        IPage<OrderBranchResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OrderBranchParam param){
        return param.getId();
    }

    private Page<OrderBranchResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OrderBranch getOldEntity(OrderBranchParam param) {
        return this.getById(getKey(param));
    }

    private OrderBranch getEntity(OrderBranchParam param) {
        OrderBranch entity = new OrderBranch();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
