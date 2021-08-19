package cn.atsoft.dasheng.portal.goods.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.goods.entity.Goods;
import cn.atsoft.dasheng.portal.goods.mapper.GoodsMapper;
import cn.atsoft.dasheng.portal.goods.model.params.GoodsParam;
import cn.atsoft.dasheng.portal.goods.model.result.GoodsResult;
import  cn.atsoft.dasheng.portal.goods.service.GoodsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 首页商品 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Override
    public void add(GoodsParam param){
        Goods entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(GoodsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(GoodsParam param){
        Goods oldEntity = getOldEntity(param);
        Goods newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public GoodsResult findBySpec(GoodsParam param){
        return null;
    }

    @Override
    public List<GoodsResult> findListBySpec(GoodsParam param){
        return null;
    }

    @Override
    public PageInfo<GoodsResult> findPageBySpec(GoodsParam param){
        Page<GoodsResult> pageContext = getPageContext();
        IPage<GoodsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(GoodsParam param){
        return param.getGoodId();
    }

    private Page<GoodsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Goods getOldEntity(GoodsParam param) {
        return this.getById(getKey(param));
    }

    private Goods getEntity(GoodsParam param) {
        Goods entity = new Goods();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
