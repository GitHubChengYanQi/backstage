package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.mapper.BrandMapper;
import cn.atsoft.dasheng.app.model.params.BrandParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import  cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Override
    public void add(BrandParam param){
        Brand entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BrandParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(BrandParam param){
        Brand oldEntity = getOldEntity(param);
        Brand newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BrandResult findBySpec(BrandParam param){
        return null;
    }

    @Override
    public List<BrandResult> findListBySpec(BrandParam param){
        return null;
    }

    @Override
    public PageInfo<BrandResult> findPageBySpec(BrandParam param){
        Page<BrandResult> pageContext = getPageContext();
        IPage<BrandResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(BrandParam param){
        return param.getBrandId();
    }

    private Page<BrandResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Brand getOldEntity(BrandParam param) {
        return this.getById(getKey(param));
    }

    private Brand getEntity(BrandParam param) {
        Brand entity = new Brand();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
