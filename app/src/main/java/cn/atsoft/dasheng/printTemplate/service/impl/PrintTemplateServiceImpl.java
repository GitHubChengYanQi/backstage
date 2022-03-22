package cn.atsoft.dasheng.printTemplate.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
import cn.atsoft.dasheng.printTemplate.mapper.PrintTemplateMapper;
import cn.atsoft.dasheng.printTemplate.model.params.PrintTemplateParam;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import cn.atsoft.dasheng.printTemplate.service.PrintTemplateService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 编辑模板 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2021-12-28
 */
@Service
public class PrintTemplateServiceImpl extends ServiceImpl<PrintTemplateMapper, PrintTemplate> implements PrintTemplateService {

    @Override
    public void add(PrintTemplateParam param) {
        PrintTemplate type = this.getOne(new QueryWrapper<PrintTemplate>() {{
            eq("type", param.getType());
        }});
        if (ToolUtil.isNotEmpty(type)) {
            throw new ServiceException(500, "已有此类模板不可重复添加，如有需求请修改之前模板");
        }
        PrintTemplate entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PrintTemplateParam param) {
        PrintTemplate printTemplate =  this.getEntity(param);
        printTemplate.setDisplay(0);
        this.updateById(printTemplate);
    }

    @Override
    public void update(PrintTemplateParam param) {

        PrintTemplate oldEntity = getOldEntity(param);
        PrintTemplate newEntity = getEntity(param);
        if (!oldEntity.getType().toString().equals(newEntity.getType().toString())) {
            throw new ServiceException(500, "不可以修改此模板");
        }
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PrintTemplateResult findBySpec(PrintTemplateParam param) {
        return null;
    }

    @Override
    public List<PrintTemplateResult> findListBySpec(PrintTemplateParam param) {
        return null;
    }

    @Override
    public PageInfo<PrintTemplateResult> findPageBySpec(DataScope dataScope, PrintTemplateParam param) {
        Page<PrintTemplateResult> pageContext = getPageContext();
        IPage<PrintTemplateResult> page = this.baseMapper.customPageList(dataScope,pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PrintTemplateParam param) {
        return param.getPrintTemplateId();
    }

    private Page<PrintTemplateResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PrintTemplate getOldEntity(PrintTemplateParam param) {
        return this.getById(getKey(param));
    }

    private PrintTemplate getEntity(PrintTemplateParam param) {
        PrintTemplate entity = new PrintTemplate();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
