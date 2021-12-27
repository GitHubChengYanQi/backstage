package cn.atsoft.dasheng.inventory.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.inventory.entity.InventoryDetail;
import cn.atsoft.dasheng.inventory.mapper.InventoryDetailMapper;
import cn.atsoft.dasheng.inventory.model.params.InventoryDetailParam;
import cn.atsoft.dasheng.inventory.model.result.InventoryDetailResult;
import  cn.atsoft.dasheng.inventory.service.InventoryDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 盘点任务详情 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-27
 */
@Service
public class InventoryDetailServiceImpl extends ServiceImpl<InventoryDetailMapper, InventoryDetail> implements InventoryDetailService {

    @Override
    public void add(InventoryDetailParam param){
        InventoryDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InventoryDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(InventoryDetailParam param){
        InventoryDetail oldEntity = getOldEntity(param);
        InventoryDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InventoryDetailResult findBySpec(InventoryDetailParam param){
        return null;
    }

    @Override
    public List<InventoryDetailResult> findListBySpec(InventoryDetailParam param){
        return null;
    }

    @Override
    public PageInfo<InventoryDetailResult> findPageBySpec(InventoryDetailParam param){
        Page<InventoryDetailResult> pageContext = getPageContext();
        IPage<InventoryDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InventoryDetailParam param){
        return param.getDetailId();
    }

    private Page<InventoryDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InventoryDetail getOldEntity(InventoryDetailParam param) {
        return this.getById(getKey(param));
    }

    private InventoryDetail getEntity(InventoryDetailParam param) {
        InventoryDetail entity = new InventoryDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
