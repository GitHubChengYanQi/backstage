package cn.atsoft.dasheng.form.pojo;

import java.util.List;

public interface RuleInterface<T> {
    DataType getType();

    List<T> getRule();
}
