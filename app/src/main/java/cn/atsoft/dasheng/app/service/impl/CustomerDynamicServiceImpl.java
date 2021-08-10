package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CustomerDynamic;
import cn.atsoft.dasheng.app.mapper.CustomerDynamicMapper;
import cn.atsoft.dasheng.app.model.params.CustomerDynamicParam;
import cn.atsoft.dasheng.app.model.result.CustomerDynamicResult;
import  cn.atsoft.dasheng.app.service.CustomerDynamicService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 客户动态表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-10
 */
@Service
public class CustomerDynamicServiceImpl extends ServiceImpl<CustomerDynamicMapper, CustomerDynamic> implements CustomerDynamicService {

    @Override
    public void add(CustomerDynamicParam param){
        CustomerDynamic entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CustomerDynamicParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CustomerDynamicParam param){
        CustomerDynamic oldEntity = getOldEntity(param);
        CustomerDynamic newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CustomerDynamicResult findBySpec(CustomerDynamicParam param){
        return null;
    }

    @Override
    public List<CustomerDynamicResult> findListBySpec(CustomerDynamicParam param){
        return null;
    }

    @Override
    public PageInfo<CustomerDynamicResult> findPageBySpec(CustomerDynamicParam param){
        Page<CustomerDynamicResult> pageContext = getPageContext();
        IPage<CustomerDynamicResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CustomerDynamicParam param){
        return param.getDynamicId();
    }

    private Page<CustomerDynamicResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CustomerDynamic getOldEntity(CustomerDynamicParam param) {
        return this.getById(getKey(param));
    }

    private CustomerDynamic getEntity(CustomerDynamicParam param) {
        CustomerDynamic entity = new CustomerDynamic();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
