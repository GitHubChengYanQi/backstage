package cn.atsoft.dasheng.uc.service.impl;


import cn.atsoft.dasheng.uc.entity.UcPayOrder;
import cn.atsoft.dasheng.uc.mapper.UcPayOrderMapper;
import cn.atsoft.dasheng.uc.model.params.UcPayOrderParam;
import cn.atsoft.dasheng.uc.model.result.UcPayOrderResult;
import cn.atsoft.dasheng.uc.service.UcPayOrderService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-03-21
 */
@Service
public class UcPayOrderServiceImpl extends ServiceImpl<UcPayOrderMapper, UcPayOrder> implements UcPayOrderService {

    @Override
    public void add(UcPayOrderParam param){
        UcPayOrder entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(UcPayOrderParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(UcPayOrderParam param){
        UcPayOrder oldEntity = getOldEntity(param);
        UcPayOrder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public UcPayOrderResult findBySpec(UcPayOrderParam param){
        return null;
    }

    @Override
    public List<UcPayOrderResult> findListBySpec(UcPayOrderParam param){
        return null;
    }

    @Override
    public PageInfo findPageBySpec(UcPayOrderParam param){
        Page<UcPayOrderResult> pageContext = getPageContext();
        IPage<UcPayOrderResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(UcPayOrderParam param){
        return param.getPayId();
    }

    private Page<UcPayOrderResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private UcPayOrder getOldEntity(UcPayOrderParam param) {
        return this.getById(getKey(param));
    }

    private UcPayOrder getEntity(UcPayOrderParam param) {
        UcPayOrder entity = new UcPayOrder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
