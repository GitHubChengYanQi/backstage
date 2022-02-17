package cn.atsoft.dasheng.daoxin.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.DaoxinPosition;
import cn.atsoft.dasheng.daoxin.mapper.DaoxinPositionMapper;
import cn.atsoft.dasheng.daoxin.model.params.DaoxinPositionParam;
import cn.atsoft.dasheng.daoxin.model.result.DaoxinPositionResult;
import  cn.atsoft.dasheng.daoxin.service.DaoxinPositionService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * daoxin职位表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-02-17
 */
@Service
public class DaoxinPositionServiceImpl extends ServiceImpl<DaoxinPositionMapper, DaoxinPosition> implements DaoxinPositionService {

    @Override
    public void add(DaoxinPositionParam param){
        DaoxinPosition entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DaoxinPositionParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DaoxinPositionParam param){
        DaoxinPosition oldEntity = getOldEntity(param);
        DaoxinPosition newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DaoxinPositionResult findBySpec(DaoxinPositionParam param){
        return null;
    }

    @Override
    public List<DaoxinPositionResult> findListBySpec(DaoxinPositionParam param){
        return null;
    }

    @Override
    public PageInfo<DaoxinPositionResult> findPageBySpec(DaoxinPositionParam param){
        Page<DaoxinPositionResult> pageContext = getPageContext();
        IPage<DaoxinPositionResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DaoxinPositionParam param){
        return param.getPositionId();
    }

    private Page<DaoxinPositionResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DaoxinPosition getOldEntity(DaoxinPositionParam param) {
        return this.getById(getKey(param));
    }

    private DaoxinPosition getEntity(DaoxinPositionParam param) {
        DaoxinPosition entity = new DaoxinPosition();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
