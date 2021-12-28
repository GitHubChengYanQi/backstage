package cn.atsoft.dasheng.Tool;

public class ComparativeCalculation {


    public Boolean calculation(int key,String operator,int value){
        Boolean flag = true;
        switch (operator){
            case "==":
                return key==value;

            case ">=":
                return key>=value;

            case "<=":
                return key<=value;

            case ">":
                return key>value;

            case "<":
                return key<value;

            case "!=":
                return key!=value;

            default:
                return false;
        }
    }
}
