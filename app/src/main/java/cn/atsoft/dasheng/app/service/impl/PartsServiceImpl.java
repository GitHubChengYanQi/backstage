package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.mapper.PartsMapper;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 清单 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Service
public class PartsServiceImpl extends ServiceImpl<PartsMapper, Parts> implements PartsService {
    @Autowired
    private ItemsService itemsService;

    @Override
    public Long add(PartsParam param) {
        Parts entity = getEntity(param);
        this.save(entity);
        return entity.getPartsId();
    }

    @Override
    public void delete(PartsParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PartsParam param) {
        Parts oldEntity = getOldEntity(param);
        Parts newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PartsResult findBySpec(PartsParam param) {
        return null;
    }

    @Override
    public List<PartsResult> findListBySpec(PartsParam param) {

        return null;
    }

    @Override
    public PageInfo<PartsResult> findPageBySpec(PartsParam param, DataScope dataScope) {
        Page<PartsResult> pageContext = getPageContext();
        IPage<PartsResult> page = this.baseMapper.customPageList(pageContext, param,dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    @Override
    public List<PartsResult> getByIds(List<Long> ids) {
        QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
        partsQueryWrapper.in("parts_id", ids);
        List<Parts> result = this.list(partsQueryWrapper);
        List<PartsResult> results = new ArrayList<>();
        for (Parts parts : result) {
            PartsResult partsResult = new PartsResult();
            ToolUtil.copyProperties(parts, partsResult);
            results.add(partsResult);
        }
        this.format(results);
        return results;
    }

    private Serializable getKey(PartsParam param) {
        return param.getPartsId();
    }

    private Page<PartsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Parts getOldEntity(PartsParam param) {
        return this.getById(getKey(param));
    }

    private Parts getEntity(PartsParam param) {
        Parts entity = new Parts();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<PartsResult> data) {
        List<Long> itemIds = new ArrayList<>();
        List<Long> iIds =new ArrayList<>();
        for (PartsResult datum : data) {
            itemIds.add(datum.getItems());
        }
        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();

        if(!itemIds.isEmpty()){
            itemsQueryWrapper.in("item_id", itemIds);
            List<Items> list = itemsService.list(itemsQueryWrapper);
            for (PartsResult datum : data) {
                for (Items items : list) {
                    if (items.getItemId().equals(datum.getItems())) {
                        ItemsResult itemsResult = new ItemsResult();
                        ToolUtil.copyProperties(items, itemsResult);
                        datum.setItemsResult(itemsResult);
                    }
                }
            }
        }
    }

}
