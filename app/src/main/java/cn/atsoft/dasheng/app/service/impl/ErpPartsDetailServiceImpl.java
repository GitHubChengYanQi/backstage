package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.mapper.ErpPartsDetailMapper;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import  cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 清单详情 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-10-26
 */
@Service
public class ErpPartsDetailServiceImpl extends ServiceImpl<ErpPartsDetailMapper, ErpPartsDetail> implements ErpPartsDetailService {

    @Override
    public void add(ErpPartsDetailParam param){
        ErpPartsDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ErpPartsDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ErpPartsDetailParam param){
        ErpPartsDetail oldEntity = getOldEntity(param);
        ErpPartsDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ErpPartsDetailResult findBySpec(ErpPartsDetailParam param){
        return null;
    }

    @Override
    public List<ErpPartsDetailResult> findListBySpec(ErpPartsDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ErpPartsDetailResult> findPageBySpec(ErpPartsDetailParam param){
        Page<ErpPartsDetailResult> pageContext = getPageContext();
        IPage<ErpPartsDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ErpPartsDetailParam param){
        return param.getPartsDetailId();
    }

    private Page<ErpPartsDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ErpPartsDetail getOldEntity(ErpPartsDetailParam param) {
        return this.getById(getKey(param));
    }

    private ErpPartsDetail getEntity(ErpPartsDetailParam param) {
        ErpPartsDetail entity = new ErpPartsDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
