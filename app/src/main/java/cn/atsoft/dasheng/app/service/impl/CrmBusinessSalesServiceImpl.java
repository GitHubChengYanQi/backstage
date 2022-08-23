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
import cn.atsoft.dasheng.app.service.CrmBusinessSalesService;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
    public void add(CrmBusinessSalesParam param) {
        CrmBusinessSales entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmBusinessSalesParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(CrmBusinessSalesParam param) {
        CrmBusinessSales oldEntity = getOldEntity(param);
        CrmBusinessSales newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessSalesResult findBySpec(CrmBusinessSalesParam param) {
        return null;
    }

    @Override
    public List<CrmBusinessSalesResult> findListBySpec(CrmBusinessSalesParam param) {

        return null;
    }

    @Override
    public PageInfo findPageBySpec(CrmBusinessSalesParam param, DataScope dataScope ) {
        Page<CrmBusinessSalesResult> pageContext = getPageContext();
        IPage<CrmBusinessSalesResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);

        this.format(page.getRecords());

        return PageFactory.createPageInfo(page);
    }

    @Override
    public CrmBusinessSalesResult detail(Long id) {
        CrmBusinessSales crmBusinessSales = this.getById(id);
        CrmBusinessSalesResult detail = new CrmBusinessSalesResult();
        ToolUtil.copyProperties(detail, crmBusinessSales);
        List<CrmBusinessSalesResult> crmBusinessSalesResultList = new ArrayList<CrmBusinessSalesResult>() {{
            add(detail);
        }};
        this.format(crmBusinessSalesResultList);
        return crmBusinessSalesResultList.get(0);
    }

    @Override
    public List<CrmBusinessSalesResult> getByIds(List<Long> ids) {
        QueryWrapper<CrmBusinessSales> crmBusinessSalesQueryWrapper = new QueryWrapper();
        crmBusinessSalesQueryWrapper.in("sales_id", ids);
        List<CrmBusinessSales> result = this.list(crmBusinessSalesQueryWrapper);
        List<CrmBusinessSalesResult> results = new ArrayList<>();
        for (CrmBusinessSales item : result) {
            CrmBusinessSalesResult tmp = new CrmBusinessSalesResult();
            ToolUtil.copyProperties(item, tmp);
            results.add(tmp);
        }
        this.format(results);
        return results;
    }

    @Override
    public void batchDelete(List<Long> ids) {
        CrmBusinessSales crmBusinessSales = new CrmBusinessSales();
        crmBusinessSales.setDisplay(0);
        QueryWrapper<CrmBusinessSales> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sales_id");
        this.update(crmBusinessSales, queryWrapper);
    }

    private Serializable getKey(CrmBusinessSalesParam param) {
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

    public void format(List<CrmBusinessSalesResult> data) {
        List<Long> salesIds = new ArrayList<>();
        for (CrmBusinessSalesResult item : data) {
            salesIds.add(item.getSalesId());
        }
        QueryWrapper<CrmBusinessSalesProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sales_id", salesIds);
        queryWrapper.orderByDesc("sort");
        List<CrmBusinessSalesProcess> res = salesIds.size() == 0 ? new ArrayList<>() : crmBusinessSalesProcessService.list(queryWrapper);

        for (CrmBusinessSalesResult item : data) {
            List<CrmBusinessSalesProcessResult> results = new ArrayList<>();
            for (CrmBusinessSalesProcess it : res) {
                if (item.getSalesId().equals(it.getSalesId())) {
                    CrmBusinessSalesProcessResult tmp = new CrmBusinessSalesProcessResult();
                    //拷贝对象
                    ToolUtil.copyProperties(it, tmp);
                    results.add(tmp);
                }
            }
            item.setProcess(results);
        }

    }
}
