package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ApplyDetails;
import cn.atsoft.dasheng.erp.mapper.ApplyDetailsMapper;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.result.ApplyDetailsResult;
import  cn.atsoft.dasheng.erp.service.ApplyDetailsService;
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
 * @author song
 * @since 2021-09-15
 */
@Service
public class ApplyDetailsServiceImpl extends ServiceImpl<ApplyDetailsMapper, ApplyDetails> implements ApplyDetailsService {

    @Override
    public void add(ApplyDetailsParam param){
        ApplyDetails entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ApplyDetailsParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ApplyDetailsParam param){
        ApplyDetails oldEntity = getOldEntity(param);
        ApplyDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ApplyDetailsResult findBySpec(ApplyDetailsParam param){
        return null;
    }

    @Override
    public List<ApplyDetailsResult> findListBySpec(ApplyDetailsParam param){
        return null;
    }

    @Override
    public PageInfo<ApplyDetailsResult> findPageBySpec(ApplyDetailsParam param){
        Page<ApplyDetailsResult> pageContext = getPageContext();
        IPage<ApplyDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ApplyDetailsParam param){
        return param.getOutstockApplyDetailsId();
    }

    private Page<ApplyDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ApplyDetails getOldEntity(ApplyDetailsParam param) {
        return this.getById(getKey(param));
    }

    private ApplyDetails getEntity(ApplyDetailsParam param) {
        ApplyDetails entity = new ApplyDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
