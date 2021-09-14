package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Plan;
import cn.atsoft.dasheng.crm.mapper.PlanMapper;
import cn.atsoft.dasheng.crm.model.params.PlanParam;
import cn.atsoft.dasheng.crm.model.result.PlanResult;
import cn.atsoft.dasheng.crm.service.PlanService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.remind.model.params.WxTemplateData;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-09-14
 */
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {

    @Override
    public void add(PlanParam param) {
        Plan entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PlanParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(PlanParam param) {
        Plan oldEntity = getOldEntity(param);
        Plan newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PlanResult findBySpec(PlanParam param) {
        return null;
    }

    @Override
    public List<PlanResult> findListBySpec(PlanParam param) {
        return null;
    }

    @Override
    public PageInfo<PlanResult> findPageBySpec(PlanParam param) {
        Page<PlanResult> pageContext = getPageContext();
        IPage<PlanResult> page = this.baseMapper.customPageList(pageContext, param);
        for (PlanResult record : page.getRecords()) {
            record.getTime();
            Date parse = (Date) JSON.parse(record.getTime());
            record.setJsonTime(parse);

        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PlanParam param) {
        return param.getSalesProcessPlanId();
    }

    private Page<PlanResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Plan getOldEntity(PlanParam param) {
        return this.getById(getKey(param));
    }

    private Plan getEntity(PlanParam param) {
        Plan entity = new Plan();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
