package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.DaoxinShipSetpClass;
import cn.atsoft.dasheng.production.mapper.DaoxinShipSetpClassMapper;
import cn.atsoft.dasheng.production.model.params.DaoxinShipSetpClassParam;
import cn.atsoft.dasheng.production.model.result.DaoxinShipSetpClassResult;
import  cn.atsoft.dasheng.production.service.DaoxinShipSetpClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工序分类表 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Service
public class DaoxinShipSetpClassServiceImpl extends ServiceImpl<DaoxinShipSetpClassMapper, DaoxinShipSetpClass> implements DaoxinShipSetpClassService {

    @Override
    public void add(DaoxinShipSetpClassParam param){
        DaoxinShipSetpClass entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DaoxinShipSetpClassParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DaoxinShipSetpClassParam param){
        DaoxinShipSetpClass oldEntity = getOldEntity(param);
        DaoxinShipSetpClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DaoxinShipSetpClassResult findBySpec(DaoxinShipSetpClassParam param){
        return null;
    }

    @Override
    public List<DaoxinShipSetpClassResult> findListBySpec(DaoxinShipSetpClassParam param){
        return null;
    }

    @Override
    public PageInfo<DaoxinShipSetpClassResult> findPageBySpec(DaoxinShipSetpClassParam param){
        Page<DaoxinShipSetpClassResult> pageContext = getPageContext();
        IPage<DaoxinShipSetpClassResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DaoxinShipSetpClassParam param){
        return param.getShipSetpClassId();
    }

    private Page<DaoxinShipSetpClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DaoxinShipSetpClass getOldEntity(DaoxinShipSetpClassParam param) {
        return this.getById(getKey(param));
    }

    private DaoxinShipSetpClass getEntity(DaoxinShipSetpClassParam param) {
        DaoxinShipSetpClass entity = new DaoxinShipSetpClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
