package cn.atsoft.dasheng.daoxin.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.Position;
import cn.atsoft.dasheng.daoxin.mapper.PositionMapper;
import cn.atsoft.dasheng.daoxin.model.params.PositionParam;
import cn.atsoft.dasheng.daoxin.model.result.PositionResult;
import  cn.atsoft.dasheng.daoxin.service.PositionService;
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
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Override
    public void add(PositionParam param){
        Position entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PositionParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PositionParam param){
        Position oldEntity = getOldEntity(param);
        Position newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PositionResult findBySpec(PositionParam param){
        return null;
    }

    @Override
    public List<PositionResult> findListBySpec(PositionParam param){
        return null;
    }

    @Override
    public PageInfo<PositionResult> findPageBySpec(PositionParam param){
        Page<PositionResult> pageContext = getPageContext();
        IPage<PositionResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PositionParam param){
        return param.getPositionId();
    }

    private Page<PositionResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Position getOldEntity(PositionParam param) {
        return this.getById(getKey(param));
    }

    private Position getEntity(PositionParam param) {
        Position entity = new Position();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
