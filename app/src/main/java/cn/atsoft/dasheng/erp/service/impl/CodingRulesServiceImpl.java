package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.entity.CodingRulesClassification;
import cn.atsoft.dasheng.erp.entity.RulesRelation;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.mapper.CodingRulesMapper;
import cn.atsoft.dasheng.erp.model.params.CodingRulesParam;
import cn.atsoft.dasheng.erp.model.params.Codings;
import cn.atsoft.dasheng.erp.model.result.CodingRulesClassificationResult;
import cn.atsoft.dasheng.erp.model.result.CodingRulesResult;
import cn.atsoft.dasheng.erp.service.CodingRulesClassificationService;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.RulesRelationService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.serial.model.params.SerialNumberParam;
import cn.atsoft.dasheng.serial.service.SerialNumberService;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.Month;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 编码规则 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-22
 */
@Service
public class CodingRulesServiceImpl extends ServiceImpl<CodingRulesMapper, CodingRules> implements CodingRulesService {
    @Autowired
    private CodingRulesClassificationService codingRulesClassificationService;
    @Autowired
    private RulesRelationService rulesRelationService;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private SpuService spuService;

    @Override
    @Transactional

    public void add(CodingRulesParam param) {

        String codingRules = "";
        if (param.getCodings().size() == 0) {
            throw new ServiceException(500, "必须定义规则！");
        } else {
            for (Codings codings : param.getCodings()) {
                if (codingRules.equals("")) {
                    codingRules = codings.getValues();
                } else {
                    codingRules = codingRules + "," + codings.getValues();
                }

            }
        }
        param.setCodingRules(codingRules);

        Integer name = this.query().in("name", param.getName()).count();

        if (name > 0) {
            throw new ServiceException(500, "不要输入重复规则名称");
        }
        CodingRules entity = getEntity(param);
        this.save(entity);
    }

    @Override

    public void delete(CodingRulesParam param) {
        this.removeById(getKey(param));
    }

    @Override
    @Transactional

    public void update(CodingRulesParam param) {

        if (ToolUtil.isNotEmpty(param.getCodings())) {
            String codingRules = "";
            if (param.getCodings().size() == 0) {
                throw new ServiceException(500, "必须定义规则！");
            } else {
                for (Codings codings : param.getCodings()) {
                    if (codingRules.equals("")) {
                        codingRules = codings.getValues();
                    } else {
                        codingRules = codingRules + "," + codings.getValues();
                    }

                }
            }
            param.setCodingRules(codingRules);
        }


//        CodingRules codingRules = new CodingRules();
//        codingRules.setState(0);
//        QueryWrapper<CodingRules> codingRulesQueryWrapper = new QueryWrapper<>();
//        codingRulesQueryWrapper.notIn("coding_rules_id", param.getCodingRulesId());
//        codingRulesQueryWrapper.in("state", param.getState());
//        this.update(codingRules, codingRulesQueryWrapper);

        CodingRules oldEntity = getOldEntity(param);
        CodingRules newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CodingRulesResult findBySpec(CodingRulesParam param) {
        return null;
    }

    @Override
    public List<CodingRulesResult> findListBySpec(CodingRulesParam param) {
        return null;
    }

    @Override
    public PageInfo<CodingRulesResult> findPageBySpec(CodingRulesParam param) {
        Page<CodingRulesResult> pageContext = getPageContext();
        IPage<CodingRulesResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<CodingRulesResult> param) {
        List<Long> classIds = new ArrayList<>();
        List<Long> rulesIds = new ArrayList<>();
        for (CodingRulesResult codingRulesResult : param) {
            classIds.add(codingRulesResult.getCodingRulesClassificationId());
            rulesIds.add(codingRulesResult.getCodingRulesId());
        }
        List<CodingRulesClassification> list = classIds.size() == 0 ? new ArrayList<>() : codingRulesClassificationService.lambdaQuery().in(CodingRulesClassification::getCodingRulesClassificationId, classIds).list();

        List<RulesRelation> rulesRelations = rulesIds.size() == 0 ? new ArrayList<>() : rulesRelationService.query().in("coding_rules_id", rulesIds).list();


        for (CodingRulesResult codingRulesResult : param) {
            for (CodingRulesClassification codingRulesClassification : list) {
                if (codingRulesResult.getCodingRulesClassificationId() != null &&
                        codingRulesClassification.getCodingRulesClassificationId() != null &&
                        codingRulesResult.getCodingRulesClassificationId().equals(codingRulesClassification.getCodingRulesClassificationId())) {
                    CodingRulesClassificationResult result = new CodingRulesClassificationResult();
                    ToolUtil.copyProperties(codingRulesClassification, result);
                    codingRulesResult.setCodingRulesClassificationResult(result);
                }
            }
            List<RulesRelation> rulesRelationList = new ArrayList<>();
            for (RulesRelation rulesRelation : rulesRelations) {

                if (rulesRelation.getCodingRulesId() != null && codingRulesResult.getCodingRulesId() != null && rulesRelation.getCodingRulesId().equals(codingRulesResult.getCodingRulesId())) {
                    rulesRelationList.add(rulesRelation);
                }
            }
            codingRulesResult.setRulesRelationList(rulesRelationList);
        }

    }

    /**
     * 生成编码
     *
     * @param ids
     * @return
     */
    @Override
    public String backCoding(Long ids) {
        String rules = "";
        CodingRules codingRules = this.getById(ids);
        if (ToolUtil.isEmpty(codingRules.getCodingRules())) {
            throw new ServiceException(500, "没有制定规则");
        }
        rules = codingRules.getCodingRules().replace(",", "");

        DateTime dateTime = new DateTime();
        //年
        int year = dateTime.year();
        //月
        Month month = dateTime.monthEnum();
        int monthValue = month.getValue();
        //随机数
        int randomInt = RandomUtil.randomInt(1, 100);
        //随机六位字符串
        String randomString = RandomUtil.randomString(6);
        //季度
        int quarter = dateTime.quarter();
        //所属年份的第几周
        int weekOfYear = dateTime.weekOfYear();
        //日
        int dayOfMonth = dateTime.dayOfMonth();
//--------------------------------------------------------------------------------------------------------------
        if (rules.contains("${dd}")) {
            rules = rules.replace("${dd}", dayOfMonth + "");
        }

        if (rules.contains("${YYYY}")) {
            rules = rules.replace("${YYYY}", year + "");
        }

        if (rules.contains("${YY}")) {
            int yy = Integer.parseInt(String.valueOf(year).substring(2));
            rules = rules.replace("${YY}", yy + "");
        }

        if (rules.contains("${MM}")) {
            rules = rules.replace("${MM}", monthValue + "");
        }

        if (rules.contains("${randomInt}")) {
            rules = rules.replace("${randomInt}", randomInt + "");
        }

        if (rules.contains("${randomString}")) {
            rules = rules.replace("${randomString}", randomString);
        }

        if (rules.contains("${quarter}")) {
            rules = rules.replace("${quarter}", quarter + "");
        }

        if (rules.contains("${week}")) {
            rules = rules.replace("${week}", weekOfYear + "");
        }


        Pattern compile = Pattern.compile("\\$\\{(serial.*?(\\[(\\d[0-9]?)\\]))\\}");
        Matcher matcher = compile.matcher(rules);
        if (matcher.find()) {
            SerialNumberParam serialNumberParam = new SerialNumberParam();
            serialNumberParam.setSerialLength(Long.valueOf(matcher.group(3)));
            String aLong = serialNumberService.add(serialNumberParam);
            rules = rules.replace(matcher.group(0) + "", aLong + "");
        }

        if (rules.contains("${skuClass}")) {
            rules = rules.replace("${skuClass}", "${skuClass}");
        }

        return rules;
    }
    @Override
    public String backSkuCoding(Long ids,Long spuId) {
        String backCoding = this.backCoding(ids);

        if (backCoding.contains("${spuCoding}")) {
            Spu spu = spuService.getById(spuId);
            backCoding = backCoding.replace("${spuCoding}", spu.getCoding());
        }
        return backCoding;
    }



    /**
     * 通过模块返回编码
     *
     * @param module
     * @return
     */
    @Override
    public String getCodingByModule(Long module) {
        CodingRules rules = this.query().eq("module", module).eq("state", 1).one();
        if (ToolUtil.isEmpty(rules)) {
            return "${type}";
        }

        return this.backCoding(rules.getCodingRulesId());

    }

    private Serializable getKey(CodingRulesParam param) {
        return param.getCodingRulesId();
    }

    private Page<CodingRulesResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CodingRules getOldEntity(CodingRulesParam param) {
        return this.getById(getKey(param));
    }

    private CodingRules getEntity(CodingRulesParam param) {
        CodingRules entity = new CodingRules();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    /**
     * 默认编码
     *
     * @return
     */
    @Override
    public String defaultEncoding() {
        DateTime dateTime = new DateTime();
        //年
        int year = dateTime.year();
        String yy = String.valueOf(Integer.parseInt(String.valueOf(year).substring(2)));

        //月
        Month month = dateTime.monthEnum();
        String monthValue = String.valueOf(month.getValue());

        //日
        String dayOfMonth = String.valueOf(dateTime.dayOfMonth());

        String serial = serialNumberService.add(new SerialNumberParam() {{  //流水号
            setSerialLength(5L);
        }});


        return yy + monthValue + dayOfMonth + serial;
    }

    @Override
    public String encoding(int module) {
        CodingRules rules = this.query().eq("module", module).eq("state", 1).one();

        if (ToolUtil.isEmpty(rules)) {
            return this.defaultEncoding();
        }

        String coding = backCoding(rules.getCodingRulesId());
        Pattern compile = Pattern.compile("\\$(.*?)\\}");
        Matcher matcher = compile.matcher(coding);

        String temp = null;
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            temp = "";
            matcher.appendReplacement(sb, temp);
            matcher.appendTail(sb);
            return sb.toString();
        }

        return coding;
    }
}
