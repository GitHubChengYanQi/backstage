package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.CrmBusinessSalesProcess;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesProcessResult;
import cn.atsoft.dasheng.app.service.CrmBusinessSalesProcessService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessSales;
import cn.atsoft.dasheng.app.mapper.CrmBusinessSalesMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesResult;
import  cn.atsoft.dasheng.app.service.CrmBusinessSalesService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 销售 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-04
 */
@Service
public class CrmBusinessSalesServiceImpl extends ServiceImpl<CrmBusinessSalesMapper, CrmBusinessSales> implements CrmBusinessSalesService {

    @Autowired
    private CrmBusinessSalesProcessService crmBusinessSalesProcessService;

    @Override
    public void add(CrmBusinessSalesParam param){
        CrmBusinessSales entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmBusinessSalesParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmBusinessSalesParam param){
        CrmBusinessSales oldEntity = getOldEntity(param);
        CrmBusinessSales newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessSalesResult findBySpec(CrmBusinessSalesParam param){
        return null;
    }

    @Override
    public List<CrmBusinessSalesResult> findListBySpec(CrmBusinessSalesParam param){
        return null;
    }

    @Override
    public PageInfo<CrmBusinessSalesResult> findPageBySpec(CrmBusinessSalesParam param){
        Page<CrmBusinessSalesResult> pageContext = getPageContext();
        IPage<CrmBusinessSalesResult> page = this.baseMapper.customPageList(pageContext, param);
        List<Long> salesIds = new ArrayList<>();
        for (CrmBusinessSalesResult  item:page.getRecords()){
            salesIds.add(item.getSalesId());
        }
        QueryWrapper<CrmBusinessSalesProcess> queryWrapper  = new QueryWrapper<>();
        queryWrapper.in("sales_id",salesIds);
        queryWrapper.orderByAsc("sort");
        List<CrmBusinessSalesProcess> res = crmBusinessSalesProcessService.list(queryWrapper);

        for (CrmBusinessSalesResult  item:page.getRecords()){
            List<CrmBusinessSalesProcessResult> results = new ArrayList<>();
            for (CrmBusinessSalesProcess it :res){
                if(item.getSalesId().equals(it.getSalesId())){
                    CrmBusinessSalesProcessResult tmp = new CrmBusinessSalesProcessResult();
                    //拷贝对象
                    ToolUtil.copyProperties(it,tmp);
                    results.add(tmp);
                }
            }
            item.setProcess(results);
        }

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessSalesParam param){
        return param.getSalesId();
    }

    private Page<CrmBusinessSalesResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmBusinessSales getOldEntity(CrmBusinessSalesParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusinessSales getEntity(CrmBusinessSalesParam param) {
        CrmBusinessSales entity = new CrmBusinessSales();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
