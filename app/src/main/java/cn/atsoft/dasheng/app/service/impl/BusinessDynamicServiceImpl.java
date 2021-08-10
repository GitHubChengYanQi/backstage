package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.BusinessDynamic;
import cn.atsoft.dasheng.app.mapper.BusinessDynamicMapper;
import cn.atsoft.dasheng.app.model.params.BusinessDynamicParam;
import cn.atsoft.dasheng.app.model.result.BusinessDynamicResult;
import  cn.atsoft.dasheng.app.service.BusinessDynamicService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商机动态表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-10
 */
@Service
public class BusinessDynamicServiceImpl extends ServiceImpl<BusinessDynamicMapper, BusinessDynamic> implements BusinessDynamicService {

    @Override
    public void add(BusinessDynamicParam param){
        BusinessDynamic entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BusinessDynamicParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(BusinessDynamicParam param){
        BusinessDynamic oldEntity = getOldEntity(param);
        BusinessDynamic newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BusinessDynamicResult findBySpec(BusinessDynamicParam param){
        return null;
    }

    @Override
    public List<BusinessDynamicResult> findListBySpec(BusinessDynamicParam param){
        return null;
    }

    @Override
    public PageInfo<BusinessDynamicResult> findPageBySpec(BusinessDynamicParam param){
        Page<BusinessDynamicResult> pageContext = getPageContext();
        IPage<BusinessDynamicResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(BusinessDynamicParam param){
        return param.getDynamicId();
    }

    private Page<BusinessDynamicResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private BusinessDynamic getOldEntity(BusinessDynamicParam param) {
        return this.getById(getKey(param));
    }

    private BusinessDynamic getEntity(BusinessDynamicParam param) {
        BusinessDynamic entity = new BusinessDynamic();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
