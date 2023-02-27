package cn.atsoft.dasheng.storehouse.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.storehouse.entity.RestStorehouse;
import cn.atsoft.dasheng.storehouse.mapper.RestStorehouseMapper;
import cn.atsoft.dasheng.storehouse.model.params.RestStorehouseParam;
import cn.atsoft.dasheng.storehouse.model.result.RestStorehouseResult;
import cn.atsoft.dasheng.storehouse.service.RestStorehouseService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 地点表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-15
 */
@Service
public class RestStorehouseServiceImpl extends ServiceImpl<RestStorehouseMapper, RestStorehouse> implements RestStorehouseService {


    @Override
    @Transactional
    public Long add(RestStorehouseParam param) {
        RestStorehouse entity = getEntity(param);
        this.save(entity);
        return entity.getStorehouseId();
    }

    @Override
    public void delete(RestStorehouseParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(RestStorehouseParam param) {
        RestStorehouse oldEntity = getOldEntity(param);
        RestStorehouse newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestStorehouseResult findBySpec(RestStorehouseParam param) {
        return null;
    }

    @Override
    public List<RestStorehouseResult> findListBySpec(RestStorehouseParam param) {
        return null;
    }

    @Override
    public PageInfo<RestStorehouseResult> findPageBySpec(RestStorehouseParam param, DataScope dataScope) {
        Page<RestStorehouseResult> pageContext = getPageContext();
        IPage<RestStorehouseResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public RestStorehouseResult getDetail(Long Id) {
        RestStorehouse storehouse = this.getById(Id);
        if (ToolUtil.isEmpty(storehouse)) {
            return null;
        }
        RestStorehouseResult storehouseResult = new RestStorehouseResult();
        ToolUtil.copyProperties(storehouse, storehouseResult);
        return storehouseResult;
    }



    private Serializable getKey(RestStorehouseParam param) {
        return param.getStorehouseId();
    }

    private Page<RestStorehouseResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("itemId");
            add("brandId");
            add("name");
            add("capacity");

        }};
        return PageFactory.defaultPage(fields);
    }

    private RestStorehouse getOldEntity(RestStorehouseParam param) {
        return this.getById(getKey(param));
    }

    private RestStorehouse getEntity(RestStorehouseParam param) {
        RestStorehouse entity = new RestStorehouse();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<RestStorehouseResult> getDetails(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<RestStorehouse> storehouses = this.listByIds(ids);
        return BeanUtil.copyToList(storehouses, RestStorehouseResult.class, new CopyOptions());
    }
}
