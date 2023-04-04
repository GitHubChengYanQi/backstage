package cn.atsoft.dasheng.wedrive.file.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum FileType {
    file_folder(1,"文件夹"),
    excel(3,"文档(表格)"),
    file(2,"文档");

    @EnumValue
    private final  Integer  value;

    private final  String  name;

    FileType( Integer value,String name) {
        this.name = name;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
