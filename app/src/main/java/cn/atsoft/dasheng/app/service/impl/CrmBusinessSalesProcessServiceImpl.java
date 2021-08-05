package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessSalesProcess;
import cn.atsoft.dasheng.app.mapper.CrmBusinessSalesProcessMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesProcessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesProcessResult;
import  cn.atsoft.dasheng.app.service.CrmBusinessSalesProcessService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 销售流程 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
@Service
public class CrmBusinessSalesProcessServiceImpl extends ServiceImpl<CrmBusinessSalesProcessMapper, CrmBusinessSalesProcess> implements CrmBusinessSalesProcessService {

    @Override
    public void add(CrmBusinessSalesProcessParam param){
        CrmBusinessSalesProcess entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmBusinessSalesProcessParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmBusinessSalesProcessParam param){
        CrmBusinessSalesProcess oldEntity = getOldEntity(param);
        CrmBusinessSalesProcess newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessSalesProcessResult findBySpec(CrmBusinessSalesProcessParam param){
        return null;
    }

    @Override
    public List<CrmBusinessSalesProcessResult> findListBySpec(CrmBusinessSalesProcessParam param){
        return null;
    }

    @Override
    public PageInfo<CrmBusinessSalesProcessResult> findPageBySpec(CrmBusinessSalesProcessParam param){
        Page<CrmBusinessSalesProcessResult> pageContext = getPageContext();
        IPage<CrmBusinessSalesProcessResult> page = this.baseMapper.customPageList(pageContext, param);


        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessSalesProcessParam param){
        return param.getSalesProcessId();
    }

    private Page<CrmBusinessSalesProcessResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmBusinessSalesProcess getOldEntity(CrmBusinessSalesProcessParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusinessSalesProcess getEntity(CrmBusinessSalesProcessParam param) {
        CrmBusinessSalesProcess entity = new CrmBusinessSalesProcess();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}