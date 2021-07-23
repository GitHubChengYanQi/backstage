package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.PartsList;
import cn.atsoft.dasheng.app.mapper.PartsListMapper;
import cn.atsoft.dasheng.app.model.params.PartsListParam;
import cn.atsoft.dasheng.app.model.result.PartsListResult;
import  cn.atsoft.dasheng.app.service.PartsListService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 零件表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-23
 */
@Service
public class PartsListServiceImpl extends ServiceImpl<PartsListMapper, PartsList> implements PartsListService {

    @Override
    public void add(PartsListParam param){
        PartsList entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PartsListParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PartsListParam param){
        PartsList oldEntity = getOldEntity(param);
        PartsList newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PartsListResult findBySpec(PartsListParam param){
        return null;
    }

    @Override
    public List<PartsListResult> findListBySpec(PartsListParam param){
        return null;
    }

    @Override
    public PageInfo<PartsListResult> findPageBySpec(PartsListParam param){
        Page<PartsListResult> pageContext = getPageContext();
        IPage<PartsListResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PartsListParam param){
        return param.getPartsListId();
    }

    private Page<PartsListResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PartsList getOldEntity(PartsListParam param) {
        return this.getById(getKey(param));
    }

    private PartsList getEntity(PartsListParam param) {
        PartsList entity = new PartsList();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
