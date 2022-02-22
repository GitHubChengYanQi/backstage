package cn.atsoft.dasheng.form.pojo;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.*;

    public enum MoneyTypeEnum {
        CNY(new HashMap<String, String>() {{
            put("english", "CNY");
            put("type", "￥");
            put("name", "人民币");
        }}),
        USD(new HashMap<String, String>() {{
            put("english", "USD");
            put("type", "$");
            put("name", "美元");
        }}),
        EUR(new HashMap<String, String>() {{
            put("english", "EUR");
            put("type", "€");
            put("name", "欧元");
        }});


        @EnumValue
        private final Map<String, String> detail;

        public Map<String, String> getDetail() {
            return detail;
        }

        @Override
        public String toString() {
            return "MoneyTypeEnum{" +
                    "detail=" + detail +
                    '}';
        }

    MoneyTypeEnum(Map<String, String> detail) {
            this.detail = detail;
        }

        public static List<Map<String, String>> enumList() {
            List<Map<String, String>> enumList = new ArrayList<>();
            for (MoneyTypeEnum value : MoneyTypeEnum.values()) {
                Map<String, String> map = new HashMap<>(value.getDetail());
                enumList.add(map);
            }
            return enumList;
        }
    }




