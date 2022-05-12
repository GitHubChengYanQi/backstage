package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.mapper.UnitMapper;
import cn.atsoft.dasheng.app.model.params.UnitParam;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import  cn.atsoft.dasheng.app.service.UnitService;
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
 * 单位表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-11
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements UnitService {

    @Override
    public void add(UnitParam param){
        Unit entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(UnitParam param){
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(UnitParam param){
        Unit oldEntity = getOldEntity(param);
        Unit newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public UnitResult findBySpec(UnitParam param){
        return null;
    }

    @Override
    public List<UnitResult> findListBySpec(UnitParam param){
        return null;
    }

    @Override
    public PageInfo<UnitResult> findPageBySpec(UnitParam param, DataScope dataScope){
        Page<UnitResult> pageContext = getPageContext();
        IPage<UnitResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(UnitParam param){
        return param.getUnitId();
    }

    private Page<UnitResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Unit getOldEntity(UnitParam param) {
        return this.getById(getKey(param));
    }

    private Unit getEntity(UnitParam param) {
        Unit entity = new Unit();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
