package cn.atsoft.dasheng.Excel.pojo;

import cn.atsoft.dasheng.crm.pojo.ContractEnum;
import cn.atsoft.dasheng.form.pojo.MoneyTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContractLabel {


    public final static List<String> labels = new ArrayList<String>() {{
        for (ContractEnum value : ContractEnum.values()) {
            add(value.getDetail());
        }
    }};


}
