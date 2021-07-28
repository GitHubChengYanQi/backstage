package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Outstock;
import cn.atsoft.dasheng.app.mapper.OutstockMapper;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.model.result.OutstockResult;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 出库表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-07-17
 */
@Service
public class OutstockServiceImpl extends ServiceImpl<OutstockMapper, Outstock> implements OutstockService {

    @Override
    public void add(OutstockParam param){
        Outstock entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(OutstockParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(OutstockParam param){
        Outstock oldEntity = getOldEntity(param);
        Outstock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public OutstockResult findBySpec(OutstockParam param){
        return null;
    }

    @Override
    public List<OutstockResult> findListBySpec(OutstockParam param){
        return null;
    }

    @Override
    public PageInfo<OutstockResult> findPageBySpec(OutstockParam param){
        Page<OutstockResult> pageContext = getPageContext();
        IPage<OutstockResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutstockParam param){
        return param.getOutstockId();
    }

    private Page<OutstockResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Outstock getOldEntity(OutstockParam param) {
        return this.getById(getKey(param));
    }

    private Outstock getEntity(OutstockParam param) {
        Outstock entity = new Outstock();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
