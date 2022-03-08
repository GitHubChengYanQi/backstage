package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.app.pojo.Lable;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.mapper.TemplateMapper;
import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.entity.ContractClass;
import cn.atsoft.dasheng.crm.model.result.ContractClassResult;
import cn.atsoft.dasheng.crm.service.ContractClassService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 合同模板 服务实现类
 * </p>
 *
 * @author
 * @since 2021-07-21
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {

    @Autowired
    private ContractClassService contractClassService;


    @Override
    public Long add(TemplateParam param) {
        Template entity = getEntity(param);
        this.save(entity);
        return entity.getTemplateId();
    }

    @Override
    public void delete(TemplateParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(TemplateParam param) {
        Template oldEntity = getOldEntity(param);
        Template newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public TemplateResult findBySpec(TemplateParam param) {
        return null;
    }

    @Override
    public List<TemplateResult> findListBySpec(TemplateParam param) {
        return null;
    }

    @Override
    public PageInfo<TemplateResult> findPageBySpec(TemplateParam param, DataScope dataScope) {
        Page<TemplateResult> pageContext = getPageContext();
        IPage<TemplateResult> page = this.baseMapper.customPageList(pageContext, param, dataScope);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<TemplateResult> data) {
        List<Long> classIds = new ArrayList<>();
        for (TemplateResult datum : data) {
            classIds.add(datum.getContractClassId());
        }
        List<ContractClass> contractClasses = contractClassService.listByIds(classIds);

        for (TemplateResult datum : data) {
            for (ContractClass contractClass : contractClasses) {
                if (ToolUtil.isNotEmpty(datum.getContractClassId()) && datum.getContractClassId().equals(contractClass.getContractClassId())) {
                    ContractClassResult classResult = new ContractClassResult();
                    ToolUtil.copyProperties(contractClass, classResult);
                    datum.setClassResult(classResult);
                    break;
                }
            }
        }
    }

    @Override
    public void batchDelete(List<Long> ids) {
        Template template = new Template();
        template.setDisplay(0);
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("template_id");
        this.update(template, queryWrapper);
    }

    private Serializable getKey(TemplateParam param) {
        return param.getTemplateId();
    }

    private Page<TemplateResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Template getOldEntity(TemplateParam param) {
        return this.getById(getKey(param));
    }

    private Template getEntity(TemplateParam param) {
        Template entity = new Template();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public Lable getLabel(Long id) {
        Template template = this.getById(id);
        if (ToolUtil.isEmpty(template)) {
            throw new ServiceException(500, "模板不存在");
        }
        String content = template.getContent();
        List<String> resultList = new ArrayList<>();

        String regStr = "\\<tr.*data-group=\"物料\"\\>([\\s\\S]*)<\\/tr>";
        String input = "\\<input (.*?)\\>";
        String td = "\\<td(.*?)\\/td\\>";

        Pattern p = Pattern.compile(regStr);
        Matcher m = p.matcher(content);//开始编译


        Lable lable = new Lable();
        List<String> inputs = new ArrayList<>();

        while (m.find()) {     //tr
            String group = m.group(0);
            Pattern tdPattern = Pattern.compile(td);
            Matcher tdMatcher = tdPattern.matcher(group);
            while (tdMatcher.find()) {
                String s = tdMatcher.group(0);
                if (s.contains("${{sku}}")) {
                    inputs.add(s);
                }
                if (s.contains("${{brand}}")) {
                    inputs.add(s);
                }
                if (s.contains("${{skuNumber}}")) {
                    inputs.add(s);
                }
                if (s.contains("${{price}}")) {
                    inputs.add(s);
                }
                Pattern compile = Pattern.compile(input);
                Matcher tdMaccher = compile.matcher(s);
                while (tdMaccher.find()) {
                    String group1 = tdMaccher.group(0);
                    if (s.contains(group1)) {
                        inputs.add(s);
                        content = content.replace(s, "");
                    }
                }
            }
        }

        Pattern compile = Pattern.compile(input);
        Matcher matcher = compile.matcher(content);
        while (matcher.find()) {    //input
            String group = matcher.group(0);
            if (group.contains("input") && group.contains("type=") && group.contains(" data-title=")) {
                resultList.add(group);
            }
        }
        lable.setStrings(resultList);


        lable.setInputs(inputs);
        return lable;
    }
}
