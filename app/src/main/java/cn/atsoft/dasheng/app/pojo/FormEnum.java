package cn.atsoft.dasheng.app.pojo;


import com.baomidou.mybatisplus.annotation.EnumValue;

public enum FormEnum implements FormService {

    ASK("采购申请") {
        @Override
        public void status(int num) {

        }

        @Override
        public void action() {

        }
    },

    InStock("入库") {
        @Override
        public void status(int num) {

        }

        @Override
        public void action() {

        }
    };


    final String type;

    FormEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "FormEnum{" +
                "type='" + type + '\'' +
                '}';
    }

    public static FormEnum getType(String type) {

        for (FormEnum value : FormEnum.values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }
}
