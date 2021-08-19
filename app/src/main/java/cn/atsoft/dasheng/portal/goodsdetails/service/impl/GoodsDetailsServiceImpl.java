package cn.atsoft.dasheng.portal.goodsdetails.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.goodsdetails.entity.GoodsDetails;
import cn.atsoft.dasheng.portal.goodsdetails.mapper.GoodsDetailsMapper;
import cn.atsoft.dasheng.portal.goodsdetails.model.params.GoodsDetailsParam;
import cn.atsoft.dasheng.portal.goodsdetails.model.result.GoodsDetailsResult;
import  cn.atsoft.dasheng.portal.goodsdetails.service.GoodsDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 首页商品详情 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
@Service
public class GoodsDetailsServiceImpl extends ServiceImpl<GoodsDetailsMapper, GoodsDetails> implements GoodsDetailsService {

    @Override
    public void add(GoodsDetailsParam param){
        GoodsDetails entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(GoodsDetailsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(GoodsDetailsParam param){
        GoodsDetails oldEntity = getOldEntity(param);
        GoodsDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public GoodsDetailsResult findBySpec(GoodsDetailsParam param){
        return null;
    }

    @Override
    public List<GoodsDetailsResult> findListBySpec(GoodsDetailsParam param){
        return null;
    }

    @Override
    public PageInfo<GoodsDetailsResult> findPageBySpec(GoodsDetailsParam param){
        Page<GoodsDetailsResult> pageContext = getPageContext();
        IPage<GoodsDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(GoodsDetailsParam param){
        return param.getGoodDetailsId();
    }

    private Page<GoodsDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private GoodsDetails getOldEntity(GoodsDetailsParam param) {
        return this.getById(getKey(param));
    }

    private GoodsDetails getEntity(GoodsDetailsParam param) {
        GoodsDetails entity = new GoodsDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
