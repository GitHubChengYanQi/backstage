package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.mapper.MaterialMapper;
import cn.atsoft.dasheng.app.model.params.MaterialParam;
import cn.atsoft.dasheng.app.model.result.MaterialResult;
import  cn.atsoft.dasheng.app.service.MaterialService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 材质 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

    @Override
    public void add(MaterialParam param){
        Material entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(MaterialParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(MaterialParam param){
        Material oldEntity = getOldEntity(param);
        Material newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public MaterialResult findBySpec(MaterialParam param){
        return null;
    }

    @Override
    public List<MaterialResult> findListBySpec(MaterialParam param){
        return null;
    }

    @Override
    public PageInfo<MaterialResult> findPageBySpec(MaterialParam param){
        Page<MaterialResult> pageContext = getPageContext();
        IPage<MaterialResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(MaterialParam param){
        return param.getMaterialId();
    }

    private Page<MaterialResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Material getOldEntity(MaterialParam param) {
        return this.getById(getKey(param));
    }

    private Material getEntity(MaterialParam param) {
        Material entity = new Material();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}