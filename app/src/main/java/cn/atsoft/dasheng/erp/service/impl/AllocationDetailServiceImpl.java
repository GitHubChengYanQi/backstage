package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationDetail;
import cn.atsoft.dasheng.erp.mapper.AllocationDetailMapper;
import cn.atsoft.dasheng.erp.model.params.AllocationDetailParam;
import cn.atsoft.dasheng.erp.model.result.AllocationDetailResult;
import  cn.atsoft.dasheng.erp.service.AllocationDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 调拨子表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-07-13
 */
@Service
public class AllocationDetailServiceImpl extends ServiceImpl<AllocationDetailMapper, AllocationDetail> implements AllocationDetailService {

    @Override
    public void add(AllocationDetailParam param){
        AllocationDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AllocationDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AllocationDetailParam param){
        AllocationDetail oldEntity = getOldEntity(param);
        AllocationDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AllocationDetailResult findBySpec(AllocationDetailParam param){
        return null;
    }

    @Override
    public List<AllocationDetailResult> findListBySpec(AllocationDetailParam param){
        return null;
    }

    @Override
    public PageInfo<AllocationDetailResult> findPageBySpec(AllocationDetailParam param){
        Page<AllocationDetailResult> pageContext = getPageContext();
        IPage<AllocationDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AllocationDetailParam param){
        return param.getAllocationDetailId();
    }

    private Page<AllocationDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AllocationDetail getOldEntity(AllocationDetailParam param) {
        return this.getById(getKey(param));
    }

    private AllocationDetail getEntity(AllocationDetailParam param) {
        AllocationDetail entity = new AllocationDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
