package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.CompanyRole;
import cn.atsoft.dasheng.crm.mapper.CompanyRoleMapper;
import cn.atsoft.dasheng.crm.model.params.CompanyRoleParam;
import cn.atsoft.dasheng.crm.model.result.CompanyRoleResult;
import  cn.atsoft.dasheng.crm.service.CompanyRoleService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 公司角色表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-09-06
 */
@Service
public class CompanyRoleServiceImpl extends ServiceImpl<CompanyRoleMapper, CompanyRole> implements CompanyRoleService {

    @Override
    public CompanyRole add(CompanyRoleParam param){
        CompanyRole entity = getEntity(param);
        this.save(entity);
        return entity;
    }

    @Override
    public void delete(CompanyRoleParam param){
        param.setDisplay(0L);
        this.update(param);
    }

    @Override
    public void update(CompanyRoleParam param){
        CompanyRole oldEntity = getOldEntity(param);
        CompanyRole newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CompanyRoleResult findBySpec(CompanyRoleParam param){
        return null;
    }

    @Override
    public List<CompanyRoleResult> findListBySpec(CompanyRoleParam param){
        return null;
    }

    @Override
    public PageInfo<CompanyRoleResult> findPageBySpec(CompanyRoleParam param){
        Page<CompanyRoleResult> pageContext = getPageContext();
        IPage<CompanyRoleResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        CompanyRole companyRole = new CompanyRole();
        companyRole.setDisplay(0L);
        QueryWrapper<CompanyRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("company_role_id");
        this.update(companyRole,queryWrapper);
    }

    private Serializable getKey(CompanyRoleParam param){
        return param.getCompanyRoleId();
    }

    private Page<CompanyRoleResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CompanyRole getOldEntity(CompanyRoleParam param) {
        return this.getById(getKey(param));
    }

    private CompanyRole getEntity(CompanyRoleParam param) {
        CompanyRole entity = new CompanyRole();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
