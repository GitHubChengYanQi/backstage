package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.mapper.BrandMapper;
import cn.atsoft.dasheng.app.model.params.BrandParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
 * 品牌表 服务实现类
 * </p>
 *
 * @author 1
 * @since 2021-07-14
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Autowired
    private PartsService partsService;

    @Override
    public Long add(BrandParam param) {
        Brand entity = getEntity(param);
        this.save(entity);
        return entity.getBrandId();
    }

    @Override
    public void delete(BrandParam param) {
      Brand byId = this.getById(param.getBrandId());
      if (ToolUtil.isEmpty(byId)){
        throw new ServiceException(500,"所删除目标不存在");
      }else {
        param.setDisplay(0);
        this.update(param);
      }
    }

    @Override
    public void update(BrandParam param) {
        Brand oldEntity = getOldEntity(param);
        Brand newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BrandResult findBySpec(BrandParam param) {
        return null;
    }

    @Override
    public List<BrandResult> findListBySpec(BrandParam param) {
        return null;
    }

    @Override
    public PageInfo<BrandResult> findPageBySpec(BrandParam param) {
        Page<BrandResult> pageContext = getPageContext();
        IPage<BrandResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(BrandParam param) {
        return param.getBrandId();
    }

    private Page<BrandResult> getPageContext() {
        List<String> fields = new ArrayList<String>(){{
            add("brandName");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Brand getOldEntity(BrandParam param) {
        return this.getById(getKey(param));
    }

    private Brand getEntity(BrandParam param) {
        Brand entity = new Brand();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<BrandResult> data) {
        List<Long> brandIs = new ArrayList<>();
        for (BrandResult datum : data) {
            brandIs.add(datum.getBrandId());
        }
        QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
        partsQueryWrapper.in("brand_id", brandIs);
        List<Parts> list = partsService.list(partsQueryWrapper);
        partsService.getByIds(brandIs);

        for (BrandResult datum : data) {
            for (Parts parts : list) {
                if (datum.getBrandId().equals(parts.getBrandId())) {
                    PartsResult partsResult =new PartsResult();
                    ToolUtil.copyProperties(parts,partsResult);
                     datum.setPartsResult(partsResult);
                     break;
                }
            }
        }
    }
}
