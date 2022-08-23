package cn.atsoft.dasheng.uc.service.impl;


import cn.atsoft.dasheng.uc.entity.UcCurrency;
import cn.atsoft.dasheng.uc.mapper.UcCurrencyMapper;
import cn.atsoft.dasheng.uc.model.params.UcCurrencyParam;
import cn.atsoft.dasheng.uc.model.result.UcCurrencyResult;
import cn.atsoft.dasheng.uc.service.UcCurrencyService;
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
 * @since 2021-03-22
 */
@Service
public class UcCurrencyServiceImpl extends ServiceImpl<UcCurrencyMapper, UcCurrency> implements UcCurrencyService {

    @Override
    public void add(UcCurrencyParam param){
        UcCurrency entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(UcCurrencyParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(UcCurrencyParam param){
        UcCurrency oldEntity = getOldEntity(param);
        UcCurrency newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public UcCurrencyResult findBySpec(UcCurrencyParam param){
        return null;
    }

    @Override
    public List<UcCurrencyResult> findListBySpec(UcCurrencyParam param){
        return null;
    }

    @Override
    public PageInfo findPageBySpec(UcCurrencyParam param){
        Page<UcCurrencyResult> pageContext = getPageContext();
        IPage<UcCurrencyResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(UcCurrencyParam param){
        return null;
    }

    private Page getPageContext() {
        return PageFactory.defaultPage();
    }

    private UcCurrency getOldEntity(UcCurrencyParam param) {
        return this.getById(getKey(param));
    }

    private UcCurrency getEntity(UcCurrencyParam param) {
        UcCurrency entity = new UcCurrency();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
