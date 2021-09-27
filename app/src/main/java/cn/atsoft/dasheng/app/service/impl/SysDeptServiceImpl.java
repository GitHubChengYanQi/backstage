package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.SysDept;
import cn.atsoft.dasheng.app.mapper.SysDeptMapper;
import cn.atsoft.dasheng.app.model.params.SysDeptParam;
import cn.atsoft.dasheng.app.model.result.SysDeptResult;
import  cn.atsoft.dasheng.app.service.SysDeptService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-12-22
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public void add(SysDeptParam param){
        SysDept entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SysDeptParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SysDeptParam param){
        SysDept oldEntity = getOldEntity(param);
        SysDept newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SysDeptResult findBySpec(SysDeptParam param){
        return null;
    }

    @Override
    public List<SysDeptResult> findListBySpec(SysDeptParam param){
        return null;
    }

    @Override
    public PageInfo<SysDeptResult> findPageBySpec(SysDeptParam param, DataScope dataScope){
        Page<SysDeptResult> pageContext = getPageContext();
        IPage<SysDeptResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SysDeptParam param){
        return param.getDeptId();
    }

    private Page<SysDeptResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SysDept getOldEntity(SysDeptParam param) {
        return this.getById(getKey(param));
    }

    private SysDept getEntity(SysDeptParam param) {
        SysDept entity = new SysDept();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
