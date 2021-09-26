package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ItemClass;
import cn.atsoft.dasheng.app.mapper.ItemClassMapper;
import cn.atsoft.dasheng.app.model.params.ItemClassParam;
import cn.atsoft.dasheng.app.model.result.ItemClassResult;
import  cn.atsoft.dasheng.app.service.ItemClassService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 产品分类表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-08-11
 */
@Service
public class ItemClassServiceImpl extends ServiceImpl<ItemClassMapper, ItemClass> implements ItemClassService {

    @Override
    public void add(ItemClassParam param){
        ItemClass entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ItemClassParam param){
      ItemClass byId = this.getById(param.getClassId());
      if (ToolUtil.isEmpty(byId)){
        throw new ServiceException(500,"删除目标不存在");
      }
      param.setDisplay(0);
      this.update(param);
    }

    @Override
    public void update(ItemClassParam param){
        ItemClass oldEntity = getOldEntity(param);
        ItemClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ItemClassResult findBySpec(ItemClassParam param){
        return null;
    }

    @Override
    public List<ItemClassResult> findListBySpec(ItemClassParam param){
        return null;
    }

    @Override
    public PageInfo<ItemClassResult> findPageBySpec(ItemClassParam param, DataScope dataScope){
        Page<ItemClassResult> pageContext = getPageContext();
        IPage<ItemClassResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ItemClassParam param){
        return param.getClassId();
    }

    private Page<ItemClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ItemClass getOldEntity(ItemClassParam param) {
        return this.getById(getKey(param));
    }

    private ItemClass getEntity(ItemClassParam param) {
        ItemClass entity = new ItemClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
