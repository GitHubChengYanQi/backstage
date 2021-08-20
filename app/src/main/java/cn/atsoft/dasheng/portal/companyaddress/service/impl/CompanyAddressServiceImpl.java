package cn.atsoft.dasheng.portal.companyaddress.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.companyaddress.entity.CompanyAddress;
import cn.atsoft.dasheng.portal.companyaddress.mapper.CompanyAddressMapper;
import cn.atsoft.dasheng.portal.companyaddress.model.params.CompanyAddressParam;
import cn.atsoft.dasheng.portal.companyaddress.model.result.CompanyAddressResult;
import  cn.atsoft.dasheng.portal.companyaddress.service.CompanyAddressService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 报修 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
@Service
public class CompanyAddressServiceImpl extends ServiceImpl<CompanyAddressMapper, CompanyAddress> implements CompanyAddressService {

    @Override
    public void add(CompanyAddressParam param){
        CompanyAddress entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CompanyAddressParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CompanyAddressParam param){
        CompanyAddress oldEntity = getOldEntity(param);
        CompanyAddress newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CompanyAddressResult findBySpec(CompanyAddressParam param){
        return null;
    }

    @Override
    public List<CompanyAddressResult> findListBySpec(CompanyAddressParam param){
        return null;
    }

    @Override
    public PageInfo<CompanyAddressResult> findPageBySpec(CompanyAddressParam param){
        Page<CompanyAddressResult> pageContext = getPageContext();
        IPage<CompanyAddressResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CompanyAddressParam param){
        return param.getCompanyId();
    }

    private Page<CompanyAddressResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CompanyAddress getOldEntity(CompanyAddressParam param) {
        return this.getById(getKey(param));
    }

    private CompanyAddress getEntity(CompanyAddressParam param) {
        CompanyAddress entity = new CompanyAddress();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
