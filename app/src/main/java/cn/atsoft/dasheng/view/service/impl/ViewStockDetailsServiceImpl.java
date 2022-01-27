package cn.atsoft.dasheng.view.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.view.entity.ViewStockDetails;
import cn.atsoft.dasheng.view.mapper.ViewStockDetailsMapper;
import cn.atsoft.dasheng.view.model.params.ViewStockDetailsParam;
import cn.atsoft.dasheng.view.model.result.ViewStockDetailsResult;
import cn.atsoft.dasheng.view.service.ViewStockDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author
 * @since 2022-01-27
 */
@Service
public class ViewStockDetailsServiceImpl extends ServiceImpl<ViewStockDetailsMapper, ViewStockDetails> implements ViewStockDetailsService {


    @Override
    public ViewStockDetailsResult findBySpec(ViewStockDetailsParam param) {
        return null;
    }

    @Override
    public List<ViewStockDetailsResult> findListBySpec(ViewStockDetailsParam param) {
        List<ViewStockDetailsResult> results = this.baseMapper.customList(param);
        return results;
    }

    @Override
    public PageInfo<ViewStockDetailsResult> findPageBySpec(ViewStockDetailsParam param) {
        Page<ViewStockDetailsResult> pageContext = getPageContext();
        IPage<ViewStockDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ViewStockDetailsParam param) {
        return null;
    }

    private Page<ViewStockDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ViewStockDetails getOldEntity(ViewStockDetailsParam param) {
        return this.getById(getKey(param));
    }

    private ViewStockDetails getEntity(ViewStockDetailsParam param) {
        ViewStockDetails entity = new ViewStockDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
