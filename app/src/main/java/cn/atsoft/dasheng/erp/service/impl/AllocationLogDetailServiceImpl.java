package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationLogDetail;
import cn.atsoft.dasheng.erp.mapper.AllocationLogDetailMapper;
import cn.atsoft.dasheng.erp.model.params.AllocationLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.AllocationLogDetailResult;
import  cn.atsoft.dasheng.erp.service.AllocationLogDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-08-11
 */
@Service
public class AllocationLogDetailServiceImpl extends ServiceImpl<AllocationLogDetailMapper, AllocationLogDetail> implements AllocationLogDetailService {

    @Override
    public void add(AllocationLogDetailParam param){
        AllocationLogDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AllocationLogDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AllocationLogDetailParam param){
        AllocationLogDetail oldEntity = getOldEntity(param);
        AllocationLogDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AllocationLogDetailResult findBySpec(AllocationLogDetailParam param){
        return null;
    }

    @Override
    public List<AllocationLogDetailResult> findListBySpec(AllocationLogDetailParam param){
        return null;
    }

    @Override
    public PageInfo<AllocationLogDetailResult> findPageBySpec(AllocationLogDetailParam param){
        Page<AllocationLogDetailResult> pageContext = getPageContext();
        IPage<AllocationLogDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AllocationLogDetailParam param){
        return param.getAllocationLogDetailId();
    }

    private Page<AllocationLogDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AllocationLogDetail getOldEntity(AllocationLogDetailParam param) {
        return this.getById(getKey(param));
    }

    private AllocationLogDetail getEntity(AllocationLogDetailParam param) {
        AllocationLogDetail entity = new AllocationLogDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
