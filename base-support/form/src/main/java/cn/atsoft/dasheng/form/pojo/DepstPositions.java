package cn.atsoft.dasheng.form.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DepstPositions<T> implements RuleInterface {

    private List<Positions> positions;
    private String label;
    private String value;

    @Override
    public DataType getType() {
        return DataType.DepstPositions;
    }

    @Override
    public List getRule() {
        return new ArrayList<T>();
    }

    /**
     * 职位
     */
    @Data
    public class Positions {
        private String label;
        private String value;
    }
}
