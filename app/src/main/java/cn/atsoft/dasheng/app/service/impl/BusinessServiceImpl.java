package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Business;
import cn.atsoft.dasheng.app.mapper.BusinessMapper;
import cn.atsoft.dasheng.app.model.params.BusinessParam;
import cn.atsoft.dasheng.app.model.result.BusinessResult;
import  cn.atsoft.dasheng.app.service.BusinessService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商机表 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-07-19
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business> implements BusinessService {

    @Override
    public void add(BusinessParam param){
        Business entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BusinessParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(BusinessParam param){
        Business oldEntity = getOldEntity(param);
        Business newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BusinessResult findBySpec(BusinessParam param){
        return null;
    }

    @Override
    public List<BusinessResult> findListBySpec(BusinessParam param){
        return null;
    }

    @Override
    public PageInfo<BusinessResult> findPageBySpec(BusinessParam param){
        Page<BusinessResult> pageContext = getPageContext();
        IPage<BusinessResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(BusinessParam param){
        return param.getBusinessId();
    }

    private Page<BusinessResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Business getOldEntity(BusinessParam param) {
        return this.getById(getKey(param));
    }

    private Business getEntity(BusinessParam param) {
        Business entity = new Business();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
