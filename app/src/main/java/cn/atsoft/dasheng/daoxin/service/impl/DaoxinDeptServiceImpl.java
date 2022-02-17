package cn.atsoft.dasheng.daoxin.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.DaoxinDept;
import cn.atsoft.dasheng.daoxin.mapper.DaoxinDeptMapper;
import cn.atsoft.dasheng.daoxin.model.params.DaoxinDeptParam;
import cn.atsoft.dasheng.daoxin.model.result.DaoxinDeptResult;
import  cn.atsoft.dasheng.daoxin.service.DaoxinDeptService;
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
public class DaoxinDeptServiceImpl extends ServiceImpl<DaoxinDeptMapper, DaoxinDept> implements DaoxinDeptService {

    @Override
    public void add(DaoxinDeptParam param){
        DaoxinDept entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DaoxinDeptParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DaoxinDeptParam param){
        DaoxinDept oldEntity = getOldEntity(param);
        DaoxinDept newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DaoxinDeptResult findBySpec(DaoxinDeptParam param){
        return null;
    }

    @Override
    public List<DaoxinDeptResult> findListBySpec(DaoxinDeptParam param){
        return null;
    }

    @Override
    public PageInfo<DaoxinDeptResult> findPageBySpec(DaoxinDeptParam param){
        Page<DaoxinDeptResult> pageContext = getPageContext();
        IPage<DaoxinDeptResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DaoxinDeptParam param){
        return param.getDeptId();
    }

    private Page<DaoxinDeptResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DaoxinDept getOldEntity(DaoxinDeptParam param) {
        return this.getById(getKey(param));
    }

    private DaoxinDept getEntity(DaoxinDeptParam param) {
        DaoxinDept entity = new DaoxinDept();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
