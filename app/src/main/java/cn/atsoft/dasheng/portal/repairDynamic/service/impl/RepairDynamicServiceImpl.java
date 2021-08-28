package cn.atsoft.dasheng.portal.repairDynamic.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repairDynamic.entity.RepairDynamic;
import cn.atsoft.dasheng.portal.repairDynamic.mapper.RepairDynamicMapper;
import cn.atsoft.dasheng.portal.repairDynamic.model.params.RepairDynamicParam;
import cn.atsoft.dasheng.portal.repairDynamic.model.result.RepairDynamicResult;
import  cn.atsoft.dasheng.portal.repairDynamic.service.RepairDynamicService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 售后动态表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-24
 */
@Service
public class RepairDynamicServiceImpl extends ServiceImpl<RepairDynamicMapper, RepairDynamic> implements RepairDynamicService {

    @Override
    public void add(RepairDynamicParam param){
        RepairDynamic entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RepairDynamicParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RepairDynamicParam param){
        RepairDynamic oldEntity = getOldEntity(param);
        RepairDynamic newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RepairDynamicResult findBySpec(RepairDynamicParam param){
        return null;
    }

    @Override
    public List<RepairDynamicResult> findListBySpec(RepairDynamicParam param){
        return null;
    }

    @Override
    public PageInfo<RepairDynamicResult> findPageBySpec(RepairDynamicParam param){
        Page<RepairDynamicResult> pageContext = getPageContext();
        IPage<RepairDynamicResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RepairDynamicParam param){
        return param.getDynamicId();
    }

    private Page<RepairDynamicResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RepairDynamic getOldEntity(RepairDynamicParam param) {
        return this.getById(getKey(param));
    }

    private RepairDynamic getEntity(RepairDynamicParam param) {
        RepairDynamic entity = new RepairDynamic();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
