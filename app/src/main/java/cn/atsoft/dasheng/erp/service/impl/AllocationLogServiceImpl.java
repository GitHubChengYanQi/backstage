package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AllocationLog;
import cn.atsoft.dasheng.erp.mapper.AllocationLogMapper;
import cn.atsoft.dasheng.erp.model.params.AllocationLogParam;
import cn.atsoft.dasheng.erp.model.result.AllocationLogResult;
import  cn.atsoft.dasheng.erp.service.AllocationLogService;
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
 * @since 2022-07-25
 */
@Service
public class AllocationLogServiceImpl extends ServiceImpl<AllocationLogMapper, AllocationLog> implements AllocationLogService {

    @Override
    public void add(AllocationLogParam param){
        AllocationLog entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AllocationLogParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(AllocationLogParam param){
        AllocationLog oldEntity = getOldEntity(param);
        AllocationLog newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AllocationLogResult findBySpec(AllocationLogParam param){
        return null;
    }

    @Override
    public List<AllocationLogResult> findListBySpec(AllocationLogParam param){
        return null;
    }

    @Override
    public PageInfo<AllocationLogResult> findPageBySpec(AllocationLogParam param){
        Page<AllocationLogResult> pageContext = getPageContext();
        IPage<AllocationLogResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AllocationLogParam param){
        return param.getAllocationLogId();
    }

    private Page<AllocationLogResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AllocationLog getOldEntity(AllocationLogParam param) {
        return this.getById(getKey(param));
    }

    private AllocationLog getEntity(AllocationLogParam param) {
        AllocationLog entity = new AllocationLog();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
