package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AppointUser<T> implements RuleInterface<T> {

    private String title;

    private String key;

    @Override
    public DataType getType() {
        return DataType.AppointUser;
    }

    @Override
    public List<T> getRule() {


        return new ArrayList<T>();
    }
}
