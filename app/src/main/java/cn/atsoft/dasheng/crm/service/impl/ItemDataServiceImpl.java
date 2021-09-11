package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.crm.entity.ItemData;
import cn.atsoft.dasheng.crm.mapper.ItemDataMapper;
import cn.atsoft.dasheng.crm.model.params.ItemDataParam;
import cn.atsoft.dasheng.crm.model.result.DataResult;
import cn.atsoft.dasheng.crm.model.result.ItemDataResult;
import cn.atsoft.dasheng.crm.service.DataService;
import cn.atsoft.dasheng.crm.service.ItemDataService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
 * 产品资料 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-09-11
 */
@Service
public class ItemDataServiceImpl extends ServiceImpl<ItemDataMapper, ItemData> implements ItemDataService {
    @Autowired
    private DataService dataService;

    @Override
    public void add(ItemDataParam param) {
        ItemData entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ItemDataParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ItemDataParam param) {
        ItemData oldEntity = getOldEntity(param);
        ItemData newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ItemDataResult findBySpec(ItemDataParam param) {
        return null;
    }

    @Override
    public List<ItemDataResult> findListBySpec(ItemDataParam param) {
        return null;
    }

    @Override
    public PageInfo<ItemDataResult> findPageBySpec(ItemDataParam param) {
        Page<ItemDataResult> pageContext = getPageContext();
        IPage<ItemDataResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ItemDataParam param) {
        return param.getItemsDataId();
    }

    private Page<ItemDataResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ItemData getOldEntity(ItemDataParam param) {
        return this.getById(getKey(param));
    }

    private ItemData getEntity(ItemDataParam param) {
        ItemData entity = new ItemData();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<ItemDataResult> data) {
        List<Long> ids = new ArrayList<>();
        for (ItemDataResult datum : data) {
            ids.add(datum.getDataId());
        }
        if (ToolUtil.isNotEmpty(ids)) {
            List<Data> dataList = dataService.lambdaQuery().in(Data::getDataId, ids).list();
            if (ToolUtil.isNotEmpty(dataList)) {
                for (ItemDataResult datum : data) {
                    for (Data dataPojo : dataList) {
                        if (dataPojo.getDataId().equals(datum.getDataId())) {
                            DataResult dataResult = new DataResult();
                            ToolUtil.copyProperties(dataPojo, dataResult);
                            datum.setDataResult(dataResult);
                        }
                    }
                }
            }
        }

    }
}
