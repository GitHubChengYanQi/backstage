package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Quotation;
import cn.atsoft.dasheng.app.mapper.QuotationMapper;
import cn.atsoft.dasheng.app.model.params.QuotationParam;
import cn.atsoft.dasheng.app.model.result.QuotationResult;
import  cn.atsoft.dasheng.app.service.QuotationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 报价表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@Service
public class QuotationServiceImpl extends ServiceImpl<QuotationMapper, Quotation> implements QuotationService {

    @Override
    public void add(QuotationParam param){
        Quotation entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QuotationParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(QuotationParam param){
        Quotation oldEntity = getOldEntity(param);
        Quotation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QuotationResult findBySpec(QuotationParam param){
        return null;
    }

    @Override
    public List<QuotationResult> findListBySpec(QuotationParam param){
        return null;
    }

    @Override
    public PageInfo<QuotationResult> findPageBySpec(QuotationParam param){
        Page<QuotationResult> pageContext = getPageContext();
        IPage<QuotationResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(QuotationParam param){
        return param.getQuotationId();
    }

    private Page<QuotationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Quotation getOldEntity(QuotationParam param) {
        return this.getById(getKey(param));
    }

    private Quotation getEntity(QuotationParam param) {
        Quotation entity = new Quotation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
