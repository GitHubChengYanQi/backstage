package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.mapper.CrmBusinessMapper;
import cn.atsoft.dasheng.app.model.params.CrmBusinessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessResult;
import  cn.atsoft.dasheng.app.service.CrmBusinessService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商机表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-03
 */
@Service
public class CrmBusinessServiceImpl extends ServiceImpl<CrmBusinessMapper, CrmBusiness> implements CrmBusinessService {

    @Override
    public void add(CrmBusinessParam param){
        CrmBusiness entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmBusinessParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmBusinessParam param){
        CrmBusiness oldEntity = getOldEntity(param);
        CrmBusiness newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmBusinessResult findBySpec(CrmBusinessParam param){
        return null;
    }

    @Override
    public List<CrmBusinessResult> findListBySpec(CrmBusinessParam param){
        return null;
    }

    @Override
    public PageInfo<CrmBusinessResult> findPageBySpec(CrmBusinessParam param){
        Page<CrmBusinessResult> pageContext = getPageContext();
        IPage<CrmBusinessResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmBusinessParam param){
        return param.getBusinessId();
    }

    private Page<CrmBusinessResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmBusiness getOldEntity(CrmBusinessParam param) {
        return this.getById(getKey(param));
    }

    private CrmBusiness getEntity(CrmBusinessParam param) {
        CrmBusiness entity = new CrmBusiness();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
