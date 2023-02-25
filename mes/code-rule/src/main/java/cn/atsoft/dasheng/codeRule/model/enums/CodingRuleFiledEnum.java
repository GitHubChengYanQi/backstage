package cn.atsoft.dasheng.codeRule.model.enums;

import cn.atsoft.dasheng.codeRule.model.RestCode;
import cn.atsoft.dasheng.codeRule.model.RestCodeRulesCategory;

import java.util.ArrayList;
import java.util.List;

import static cn.atsoft.dasheng.codeRule.model.enums.CodingRuleFiledEnum.modelEnum.*;

public enum CodingRuleFiledEnum {


    //全局通用
    YYYY("四位数年","${YYYY}", general),
    YY("两位数年","${YY}", general),
    MM("两位数月","${MM}", general),
    dd("两位数日","${dd}", general),
    randomInt("随机数","${randomInt}", general),
    week("当前日期所属年份的第几周","${week}", general),
    randomString("随机字符串","${randomString}", general),
    quarter("当前季度","${quarter}", general),
    serial("流水号","${serial}", general),

    //sku
    skuClass("分类码","${skuClass}", sku),
    spuCoding("产品码","${spuCoding}", sku);


     final String label;

     final String value;

     final modelEnum model;

    CodingRuleFiledEnum(String label , String value, modelEnum model) {
        this.label = label;
        this.value = value;
        this.model = model;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public modelEnum getModel() {
        return model;
    }

    /**
     * 根据model来获取集合
     * @param model
     * @return
     */
    public static List<RestCode> listByModel(modelEnum model){

        List<RestCode> result = new ArrayList<>();
        for (CodingRuleFiledEnum value : CodingRuleFiledEnum.values()) {
            if (value.getModel().equals(model)){
                result.add(new RestCode(){{
                    setLabel(value.getLabel());
                    setValue(value.getValue());
                }});
            }
        }
        return result;
    }

    /**
     * 获取所有集合
     * @return
     */
    public static List<RestCode> allList(){
        List<RestCode> result = new ArrayList<>();
        for (CodingRuleFiledEnum value : CodingRuleFiledEnum.values()) {
            result.add(new RestCode(){{
                setLabel(value.getLabel());
                setValue(value.getValue());
            }});
        }
        return result;
    }
    /**
     * 获取非公用
     * @return
     */
    public static List<RestCodeRulesCategory> modelList(){

        List<RestCodeRulesCategory> codeRulesCategories = new ArrayList<>();
        for (modelEnum value : modelEnum.values()) {
            if (!value.equals(general)){
                RestCodeRulesCategory restCodeRulesCategory = new RestCodeRulesCategory();
                restCodeRulesCategory.setName(value.getName());
                restCodeRulesCategory.setModule(value.toString());

                List<RestCode> result = new ArrayList<>();
                for (CodingRuleFiledEnum codingRuleFiledEnum : CodingRuleFiledEnum.values()) {
                    if (!codingRuleFiledEnum.getModel().equals(general)){
                        result.add(new RestCode(){{
                            setLabel(codingRuleFiledEnum.getLabel());
                            setValue(codingRuleFiledEnum.getValue());
                        }});
                    }
                }
                restCodeRulesCategory.setRuleList(result);
                codeRulesCategories.add(restCodeRulesCategory);
            }


        }
        return codeRulesCategories;

    }

    public enum modelEnum {
        general("通用"),sku("物料");
        final String name;

        modelEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
