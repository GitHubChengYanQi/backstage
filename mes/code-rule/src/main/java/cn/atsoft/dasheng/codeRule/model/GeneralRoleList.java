package cn.atsoft.dasheng.codeRule.model;

import java.util.ArrayList;
import java.util.List;

public class GeneralRoleList {
    public static List<RestCode> generalRole(){
        return new ArrayList<RestCode>() {{
           new RestCode() {{
                add(new RestCode() {{
                    setLabel("四位数年");
                    setValue("${YYYY}");
                }});
                add(new RestCode() {{
                    setLabel("两位数年");
                    setValue("${YY}");
                }});

                add(new RestCode() {{
                    setLabel("两位数月");
                    setValue("${MM}");
                }});

                add(new RestCode() {{
                    setLabel("两位数日");
                    setValue("${dd}");
                }});
                add(new RestCode() {{
                    setLabel("随机数");
                    setValue("${randomInt}");
                }});
                add(new RestCode() {{
                    setLabel("当前日期所属年份的第几周");
                    setValue("${week}");
                }});
                add(new RestCode() {{
                    setLabel("随机字符串");
                    setValue("${randomString}");
                }});

                add(new RestCode() {{
                    setLabel("当前季度");
                    setValue("${quarter}");
                }});
                add(new RestCode() {{
                    setLabel("流水号");
                    setValue("serial");
                }});

            }};


        }};
    }
}
