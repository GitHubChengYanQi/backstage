package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.mapper.StorehouseMapper;
import cn.atsoft.dasheng.app.model.params.StorehouseParam;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StorehouseServiceImpl extends ServiceImpl<StorehouseMapper, Storehouse> implements StorehouseService {


    @Override
    @Transactional
    public Long add(StorehouseParam param) {
        Storehouse entity = getEntity(param);
        this.save(entity);
        return entity.getStorehouseId();
    }

    @Override
    public void delete(StorehouseParam param) {
        param.setDisplay(0);
        this.updateById(getEntity(param));
    }

    @Override
    public void update(StorehouseParam param) {
        Storehouse oldEntity = getOldEntity(param);
        Storehouse newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public StorehouseResult findBySpec(StorehouseParam param) {
        return null;
    }

    @Override
    public List<StorehouseResult> findListBySpec(StorehouseParam param) {
        return null;
    }

    @Override
    public PageInfo<StorehouseResult> findPageBySpec(StorehouseParam param, DataScope dataScope) {
        Page<StorehouseResult> pageContext = getPageContext();
        IPage<StorehouseResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public StorehouseResult getDetail(Long Id) {
        Storehouse storehouse = this.getById(Id);
        if (ToolUtil.isEmpty(storehouse)) {
            return null;
        }
        StorehouseResult storehouseResult = new StorehouseResult();

        ToolUtil.copyProperties(storehouse, storehouseResult);

        return storehouseResult;
    }

    private Serializable getKey(StorehouseParam param) {
        return param.getStorehouseId();
    }

    private Page<StorehouseResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("itemId");
            add("brandId");
            add("name");
            add("capacity");

        }};
        return PageFactory.defaultPage(fields);
    }

    private Storehouse getOldEntity(StorehouseParam param) {
        return this.getById(getKey(param));
    }

    private Storehouse getEntity(StorehouseParam param) {
        Storehouse entity = new Storehouse();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<StorehouseResult> getDetails(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Storehouse> storehouses = this.listByIds(ids);
        return BeanUtil.copyToList(storehouses, StorehouseResult.class, new CopyOptions());
    }
}
