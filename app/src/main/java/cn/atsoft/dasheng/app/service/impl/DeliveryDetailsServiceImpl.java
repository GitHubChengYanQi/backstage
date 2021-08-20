package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.DeliveryDetails;
import cn.atsoft.dasheng.app.mapper.DeliveryDetailsMapper;
import cn.atsoft.dasheng.app.model.params.DeliveryDetailsParam;
import cn.atsoft.dasheng.app.model.result.DeliveryDetailsResult;
import  cn.atsoft.dasheng.app.service.DeliveryDetailsService;
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
 * @author  
 * @since 2021-08-20
 */
@Service
public class DeliveryDetailsServiceImpl extends ServiceImpl<DeliveryDetailsMapper, DeliveryDetails> implements DeliveryDetailsService {

    @Override
    public void add(DeliveryDetailsParam param){
        DeliveryDetails entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(DeliveryDetailsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(DeliveryDetailsParam param){
        DeliveryDetails oldEntity = getOldEntity(param);
        DeliveryDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public DeliveryDetailsResult findBySpec(DeliveryDetailsParam param){
        return null;
    }

    @Override
    public List<DeliveryDetailsResult> findListBySpec(DeliveryDetailsParam param){
        return null;
    }

    @Override
    public PageInfo<DeliveryDetailsResult> findPageBySpec(DeliveryDetailsParam param){
        Page<DeliveryDetailsResult> pageContext = getPageContext();
        IPage<DeliveryDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(DeliveryDetailsParam param){
        return param.getDeliveryDetailsId();
    }

    private Page<DeliveryDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private DeliveryDetails getOldEntity(DeliveryDetailsParam param) {
        return this.getById(getKey(param));
    }

    private DeliveryDetails getEntity(DeliveryDetailsParam param) {
        DeliveryDetails entity = new DeliveryDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
