package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiSetpSetDetail;
import cn.atsoft.dasheng.form.mapper.ActivitiSetpSetDetailMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiSetpSetDetailParam;
import cn.atsoft.dasheng.form.model.result.ActivitiSetpSetDetailResult;
import  cn.atsoft.dasheng.form.service.ActivitiSetpSetDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 工序步骤详情表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class ActivitiSetpSetDetailServiceImpl extends ServiceImpl<ActivitiSetpSetDetailMapper, ActivitiSetpSetDetail> implements ActivitiSetpSetDetailService {

    @Override
    public void add(ActivitiSetpSetDetailParam param){
        ActivitiSetpSetDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ActivitiSetpSetDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ActivitiSetpSetDetailParam param){
        ActivitiSetpSetDetail oldEntity = getOldEntity(param);
        ActivitiSetpSetDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ActivitiSetpSetDetailResult findBySpec(ActivitiSetpSetDetailParam param){
        return null;
    }

    @Override
    public List<ActivitiSetpSetDetailResult> findListBySpec(ActivitiSetpSetDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ActivitiSetpSetDetailResult> findPageBySpec(ActivitiSetpSetDetailParam param){
        Page<ActivitiSetpSetDetailResult> pageContext = getPageContext();
        IPage<ActivitiSetpSetDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ActivitiSetpSetDetailParam param){
        return param.getDetailId();
    }

    private Page<ActivitiSetpSetDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ActivitiSetpSetDetail getOldEntity(ActivitiSetpSetDetailParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiSetpSetDetail getEntity(ActivitiSetpSetDetailParam param) {
        ActivitiSetpSetDetail entity = new ActivitiSetpSetDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
