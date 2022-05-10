package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmIndustry;
import cn.atsoft.dasheng.app.mapper.CrmIndustryMapper;
import cn.atsoft.dasheng.app.model.params.CrmIndustryParam;
import cn.atsoft.dasheng.app.model.result.CrmIndustryResult;
import cn.atsoft.dasheng.app.service.CrmIndustryService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 行业表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-08-02
 */
@Service
public class CrmIndustryServiceImpl extends ServiceImpl<CrmIndustryMapper, CrmIndustry> implements CrmIndustryService {

    @Override
    public void add(CrmIndustryParam param) {
        CrmIndustry entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmIndustryParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(CrmIndustryParam param) {
        CrmIndustry oldEntity = getOldEntity(param);
        CrmIndustry newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmIndustryResult findBySpec(CrmIndustryParam param) {
        return null;
    }

    @Override
    public List<CrmIndustryResult> findListBySpec(CrmIndustryParam param) {
        return null;
    }

    @Override
    public PageInfo<CrmIndustryResult> findPageBySpec(CrmIndustryParam param, DataScope dataScope) {
        Page<CrmIndustryResult> pageContext = getPageContext();
        IPage<CrmIndustryResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);

        List<Long> pids = new ArrayList<>();
        for (CrmIndustryResult item : page.getRecords()) {
            pids.add(item.getParentId());
        }
        QueryWrapper<CrmIndustry> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("industry_id", pids);
        List<CrmIndustry> res = this.list(queryWrapper);

        for (CrmIndustryResult item : page.getRecords()) {
            for (CrmIndustry it : res) {
                if (item.getParentId() != null && item.getParentId().equals(it.getIndustryId())) {
                    item.setParentName(it.getIndustryName());
                }
            }
        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmIndustryParam param) {
        return param.getIndustryId();
    }

    private Page<CrmIndustryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmIndustry getOldEntity(CrmIndustryParam param) {
        return this.getById(getKey(param));
    }

    private CrmIndustry getEntity(CrmIndustryParam param) {
        CrmIndustry entity = new CrmIndustry();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void batchDelete(List<Long> ids) {
        CrmIndustry crmIndustry = new CrmIndustry();
        crmIndustry.setDisplay(0);
        QueryWrapper<CrmIndustry> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("indusry_id");
        this.update(crmIndustry, queryWrapper);
    }
}
