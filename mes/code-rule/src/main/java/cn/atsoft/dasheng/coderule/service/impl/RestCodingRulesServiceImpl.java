package cn.atsoft.dasheng.coderule.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.entity.RestCodingRules;
import cn.atsoft.dasheng.coderule.model.RestCodeRulesCategory;
import cn.atsoft.dasheng.coderule.mapper.RestCodingRulesMapper;
import cn.atsoft.dasheng.coderule.model.RestCode;
import cn.atsoft.dasheng.coderule.model.params.RestCodingRulesParam;
import cn.atsoft.dasheng.coderule.model.params.RestSerialNumberParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodingRulesCategoryResult;
import cn.atsoft.dasheng.coderule.model.result.RestCodingRulesResult;
import cn.atsoft.dasheng.coderule.service.RestCodingRulesCategoryService;
import cn.atsoft.dasheng.coderule.service.RestCodingRulesService;
import cn.atsoft.dasheng.coderule.service.RestSerialNumberService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Month;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
public class RestCodingRulesServiceImpl extends ServiceImpl<RestCodingRulesMapper, RestCodingRules> implements RestCodingRulesService {
    @Autowired
    private RestCodingRulesCategoryService codingRulesClassificationService;

    @Autowired
    private RestSerialNumberService serialNumberService;



    @Override
    @Transactional

    public void add(RestCodingRulesParam param) {

        String codingRules = "";
        if (param.getCodings().size() == 0) {
            throw new ServiceException(500, "必须定义规则！");
        } else {
            for (RestCode codings : param.getCodings()) {
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
        RestCodingRules entity = getEntity(param);
        this.save(entity);
    }

    @Override

    public void delete(RestCodingRulesParam param) {
        this.removeById(this.getEntity(param));
    }

    @Override
    @Transactional

    public void update(RestCodingRulesParam param) {
        updateDefault(param.getCodingRulesId());
        if (ToolUtil.isNotEmpty(param.getCodings())) {
            String codingRules = "";
            if (param.getCodings().size() == 0) {
                throw new ServiceException(500, "必须定义规则！");
            } else {
                for (RestCode codings : param.getCodings()) {
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

        RestCodingRules oldEntity = getOldEntity(param);
        RestCodingRules newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    /**
     * 修改吗默认
     *
     * @param id
     */
    private void updateDefault(Long id) {
        RestCodingRules rules = ToolUtil.isEmpty(id) ? new RestCodingRules() : this.getById(id);
        if (ToolUtil.isNotEmpty(rules)) {
            QueryWrapper<RestCodingRules> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("module", rules.getModule());
            this.update(new RestCodingRules() {{
                setState(0);
            }}, queryWrapper);
            rules.setState(1);
            this.updateById(rules);
        }

    }

    @Override
    public RestCodingRulesResult findBySpec(RestCodingRulesParam param) {
        return null;
    }

    @Override
    public List<RestCodingRulesResult> findListBySpec(RestCodingRulesParam param) {
        return null;
    }

    @Override
    public PageInfo<RestCodingRulesResult> findPageBySpec(RestCodingRulesParam param) {
        Page<RestCodingRulesResult> pageContext = getPageContext();
        IPage<RestCodingRulesResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<RestCodingRulesResult> param) {
        List<Long> classIds = new ArrayList<>();
        List<Long> rulesIds = new ArrayList<>();
        for (RestCodingRulesResult codingRulesResult : param) {
            classIds.add(codingRulesResult.getCodingRulesClassificationId());
            rulesIds.add(codingRulesResult.getCodingRulesId());
        }
        List<RestCodeRulesCategory> list = classIds.size() == 0 ? new ArrayList<>() : codingRulesClassificationService.lambdaQuery().in(RestCodeRulesCategory::getCodingRulesClassificationId, classIds).list();



        for (RestCodingRulesResult codingRulesResult : param) {
            for (RestCodeRulesCategory codingRulesClassification : list) {
                if (codingRulesResult.getCodingRulesClassificationId() != null &&
                        codingRulesClassification.getCodingRulesClassificationId() != null &&
                        codingRulesResult.getCodingRulesClassificationId().equals(codingRulesClassification.getCodingRulesClassificationId())) {
                    RestCodingRulesCategoryResult result = new RestCodingRulesCategoryResult();
                    ToolUtil.copyProperties(codingRulesClassification, result);
                    codingRulesResult.setCodingRulesClassificationResult(result);
                }
            }
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
        return this.backCoding(ids, null);
    }

    @Override
    public String backCoding(Long ids, Long spuId) {
        String rules = "";
        RestCodingRules codingRules = this.getById(ids);
        if (ToolUtil.isEmpty(codingRules.getCodingRules())) {
            throw new ServiceException(500, "没有制定规则");
        }
        rules = codingRules.getCodingRules().replace(",", "");
        Date date = new Date();
        //年
        int year = DateUtil.year(date);
        //月
        Month month = DateUtil.monthEnum(date);
        String monthValue = String.format("%02d", month.getValueBaseOne());

        //随机数
        int randomInt = RandomUtil.randomInt(1, 100);
        //随机六位字符串
        String randomString = RandomUtil.randomString(6);
        //季度
        int quarter = DateUtil.quarter(date);

        //所属年份的第几周
//        int weekOfYear = dateTime.weekOfYear();
        String weekOfYear = String.format("%02d", DateUtil.weekOfYear(date));
        //日
//        int dayOfMonth = dateTime.dayOfMonth();
        String dayOfMonth = String.format("%02d", DateUtil.dayOfMonth(date));
//--------------------------------------------------------------------------------------------------------------
        StringBuffer stringBuffer = new StringBuffer();

        if (rules.contains("${YYYY}")) {
            rules = rules.replace("${YYYY}", year + "");
            stringBuffer.append(year);
        }

        if (rules.contains("${YY}")) {
            int yy = Integer.parseInt(String.valueOf(year).substring(2));
            rules = rules.replace("${YY}", yy + "");
            stringBuffer.append(yy);
        }

        if (rules.contains("${MM}")) {
            rules = rules.replace("${MM}", monthValue + "");
            stringBuffer.append(monthValue);
        }

        if (rules.contains("${dd}")) {
            rules = rules.replace("${dd}", dayOfMonth + "");
            stringBuffer.append(dayOfMonth);
        }

        if (rules.contains("${week}")) {
            rules = rules.replace("${week}", weekOfYear + "");
            stringBuffer.append(weekOfYear);
        }

        if (rules.contains("${randomInt}")) {
            rules = rules.replace("${randomInt}", randomInt + "");
            stringBuffer.append(randomInt);
        }

        if (rules.contains("${randomString}")) {
            rules = rules.replace("${randomString}", randomString);
            stringBuffer.append(randomString);
        }

        if (rules.contains("${quarter}")) {
            rules = rules.replace("${quarter}", quarter + "");
            stringBuffer.append(quarter);
        }

        Pattern compile = Pattern.compile("\\$\\{(serial.*?(\\[(\\d[0-9]?)\\]))\\}");
        Matcher matcher = compile.matcher(rules);

        if (matcher.find()) {
            RestSerialNumberParam serialNumberParam = new RestSerialNumberParam();
            String md5 = SecureUtil.md5(stringBuffer.toString());
            serialNumberParam.setMd5(md5);
            serialNumberParam.setSerialLength(Long.valueOf(matcher.group(3)));
            String aLong = serialNumberService.add(serialNumberParam);
            rules = rules.replace(matcher.group(0) + "", aLong + "");
        }
        return rules;
    }


    /**
     * 通过模块返回编码
     *
     * @param module
     * @return
     */
    @Override
    public String getCodingByModule(Long module) {
        RestCodingRules rules = this.query().eq("module", module).eq("state", 1).one();
        if (ToolUtil.isEmpty(rules)) {
            return "${type}";
        }
        return this.backCoding(rules.getCodingRulesId());

    }

    private Serializable getKey(RestCodingRulesParam param) {
        return param.getCodingRulesId();
    }

    private Page<RestCodingRulesResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestCodingRules getOldEntity(RestCodingRulesParam param) {
        return this.getById(getKey(param));
    }

    private RestCodingRules getEntity(RestCodingRulesParam param) {
        RestCodingRules entity = new RestCodingRules();
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

        String serial = serialNumberService.add(new RestSerialNumberParam() {{  //流水号
            setSerialLength(5L);
        }});


        return yy + monthValue + dayOfMonth + serial;
    }

    @Override
    public String encoding(int module) {
        RestCodingRules rules = this.query().eq("module", module).eq("state", 1).one();

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
