package cn.atsoft.dasheng.daoxin.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.Dept;
import cn.atsoft.dasheng.daoxin.mapper.DeptMapper;
import cn.atsoft.dasheng.daoxin.model.params.DeptParam;
import cn.atsoft.dasheng.daoxin.model.result.DeptResult;
import  cn.atsoft.dasheng.daoxin.service.DeptService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * daoxin部门表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-17
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public void add(DeptParam param){
        Dept entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DeptParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DeptParam param){
        Dept oldEntity = getOldEntity(param);
        Dept newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DeptResult findBySpec(DeptParam param){
        return null;
    }

    @Override
    public List<DeptResult> findListBySpec(DeptParam param){
        return null;
    }

    @Override
    public PageInfo<DeptResult> findPageBySpec(DeptParam param){
        Page<DeptResult> pageContext = getPageContext();
        IPage<DeptResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DeptParam param){
        return param.getDeptId();
    }

    private Page<DeptResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Dept getOldEntity(DeptParam param) {
        return this.getById(getKey(param));
    }

    private Dept getEntity(DeptParam param) {
        Dept entity = new Dept();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
