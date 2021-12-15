package cn.atsoft.dasheng.Tool;

public class ComparativeCalculation {


    public Boolean calculation(int key,String operator,int value){
        Boolean flag = true;
        switch (operator){
            case "==":
                flag = key==value;
                break;
            case ">=":
                flag = key>=value;
                break;
            case "<=":
                flag=key<=value;
                break;
            case ">":
                flag = key>value;
                break;
            case "<":
                flag=key<value;
                break;
            case "!=":
                flag=key!=value;
                break;
        }
        return flag;
    }
}
