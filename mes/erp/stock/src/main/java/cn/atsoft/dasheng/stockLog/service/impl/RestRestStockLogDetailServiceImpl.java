package cn.atsoft.dasheng.stockLog.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.stockLog.entity.RestStockLogDetail;
import cn.atsoft.dasheng.stockLog.mapper.RestStockLogDetailMapper;
import cn.atsoft.dasheng.stockLog.model.params.RestStockLogDetailParam;
import cn.atsoft.dasheng.stockLog.model.result.RestStockLogDetailResult;
import cn.atsoft.dasheng.stockLog.service.RestStockLogDetailService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 库存操作记录子表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-12-22
 */
@Service
public class RestRestStockLogDetailServiceImpl extends ServiceImpl<RestStockLogDetailMapper, RestStockLogDetail> implements RestStockLogDetailService {

    @Override
    public void add(RestStockLogDetailParam param){
        RestStockLogDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(RestStockLogDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(RestStockLogDetailParam param){
        RestStockLogDetail oldEntity = getOldEntity(param);
        RestStockLogDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public RestStockLogDetailResult findBySpec(RestStockLogDetailParam param){
        return null;
    }

    @Override
    public List<RestStockLogDetailResult> findListBySpec(RestStockLogDetailParam param){
        return null;
    }

    @Override
    public PageInfo<RestStockLogDetailResult> findPageBySpec(RestStockLogDetailParam param){
        Page<RestStockLogDetailResult> pageContext = getPageContext();
        IPage<RestStockLogDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestStockLogDetailParam param){
        return param.getStockLogDetailId();
    }

    private Page<RestStockLogDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestStockLogDetail getOldEntity(RestStockLogDetailParam param) {
        return this.getById(getKey(param));
    }

    private RestStockLogDetail getEntity(RestStockLogDetailParam param) {
        RestStockLogDetail entity = new RestStockLogDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
