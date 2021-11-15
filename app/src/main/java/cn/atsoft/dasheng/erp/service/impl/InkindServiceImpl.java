package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Inkind;
import cn.atsoft.dasheng.erp.mapper.InkindMapper;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.result.BackSku;
import cn.atsoft.dasheng.erp.model.result.InkindResult;
import cn.atsoft.dasheng.erp.service.InkindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 实物表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-11-01
 */
@Service
public class InkindServiceImpl extends ServiceImpl<InkindMapper, Inkind> implements InkindService {
    @Autowired
    private SkuService skuService;
    @Autowired
    private BrandService brandService;

    @Override
    public Long add(InkindParam param) {
        Inkind entity = getEntity(param);
        this.save(entity);
        return entity.getInkindId();
    }

    @Override

    public void delete(InkindParam param) {
        this.removeById(getKey(param));
    }

    @Override

    public void update(InkindParam param) {
        Inkind oldEntity = getOldEntity(param);
        Inkind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InkindResult findBySpec(InkindParam param) {
        return null;
    }

    @Override
    public List<InkindResult> findListBySpec(InkindParam param) {
        return null;
    }

    @Override
    public PageInfo<InkindResult> findPageBySpec(InkindParam param) {
        Page<InkindResult> pageContext = getPageContext();
        IPage<InkindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public InkindResult backInKindgetById(Long id) {
        Inkind inkind = this.getById(id);
        InkindResult inkindResult = new InkindResult();
        ToolUtil.copyProperties(inkind, inkindResult);
        List<BackSku> backSku = skuService.backSku(inkindResult.getSkuId());
        Brand brand = brandService.query().eq("brand_id", inkindResult.getBrandId()).one();
        inkindResult.setBrand(brand);
        inkindResult.setBackSku(backSku);
        return inkindResult;
    }

    private Serializable getKey(InkindParam param) {
        return param.getInkindId();
    }

    private Page<InkindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Inkind getOldEntity(InkindParam param) {
        return this.getById(getKey(param));
    }

    private Inkind getEntity(InkindParam param) {
        Inkind entity = new Inkind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }


}
