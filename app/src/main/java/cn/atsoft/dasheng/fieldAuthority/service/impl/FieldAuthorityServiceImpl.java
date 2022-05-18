package cn.atsoft.dasheng.fieldAuthority.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.fieldAuthority.entity.FieldAuthority;
import cn.atsoft.dasheng.fieldAuthority.mapper.FieldAuthorityMapper;
import cn.atsoft.dasheng.fieldAuthority.model.params.FieldAuthorityParam;
import cn.atsoft.dasheng.fieldAuthority.model.result.FieldAuthorityResult;
import cn.atsoft.dasheng.fieldAuthority.service.FieldAuthorityService;

import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 字段操作权限表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-05-17
 */
@Service
public class FieldAuthorityServiceImpl extends ServiceImpl<FieldAuthorityMapper, FieldAuthority> implements FieldAuthorityService {

    @Override
    public void add(FieldAuthorityParam param){
        FieldAuthority entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(FieldAuthorityParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(FieldAuthorityParam param){
        FieldAuthority oldEntity = getOldEntity(param);
        FieldAuthority newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public FieldAuthorityResult findBySpec(FieldAuthorityParam param){
        return null;
    }

    @Override
    public List<FieldAuthorityResult> findListBySpec(FieldAuthorityParam param){
        return null;
    }

    @Override
    public PageInfo<FieldAuthorityResult> findPageBySpec(FieldAuthorityParam param){
        Page<FieldAuthorityResult> pageContext = getPageContext();
        IPage<FieldAuthorityResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(FieldAuthorityParam param){
        return param.getAuthorityId();
    }

    private Page<FieldAuthorityResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private FieldAuthority getOldEntity(FieldAuthorityParam param) {
        return this.getById(getKey(param));
    }

    private FieldAuthority getEntity(FieldAuthorityParam param) {
        FieldAuthority entity = new FieldAuthority();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
