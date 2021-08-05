package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessDetailed;
import cn.atsoft.dasheng.app.mapper.CrmBusinessDetailedMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessDetailedResult;
import  cn.atsoft.dasheng.app.service.CrmBusinessDetailedService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商机明细表 服务实现类
 * </p>
 *
 * @author qr
 * @since 2021-08-04
 */
@Service
public class CrmBusinessDetailedServiceImpl extends ServiceImpl<CrmBusinessDetailedMapper, CrmBusinessDetailed> implements CrmBusinessDetailedService {

    @Override
    public void add(CrmBusinessDetailedParam param){
        CrmBusinessDetailed entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmBusinessDetailedParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmBusinessDetailedParam param){
        CrmBusinessDetailed oldEntity = getOldEntity(param);
        CrmBusinessDetailed newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessDetailedResult findBySpec(CrmBusinessDetailedParam param){
        return null;
    }

    @Override
    public List<CrmBusinessDetailedResult> findListBySpec(CrmBusinessDetailedParam param){
        return null;
    }

    @Override
    public PageInfo<CrmBusinessDetailedResult> findPageBySpec(CrmBusinessDetailedParam param){
        Page<CrmBusinessDetailedResult> pageContext = getPageContext();
        IPage<CrmBusinessDetailedResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessDetailedParam param){
        return param.getId();
    }

    private Page<CrmBusinessDetailedResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmBusinessDetailed getOldEntity(CrmBusinessDetailedParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusinessDetailed getEntity(CrmBusinessDetailedParam param) {
        CrmBusinessDetailed entity = new CrmBusinessDetailed();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}