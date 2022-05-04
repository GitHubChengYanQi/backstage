package cn.atsoft.dasheng.form.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ProcessType {
    QUALITY("质检" ,"quality", new ArrayList<Map<String,String>>(){{
        add(new HashMap<String,String>(){{
            put("moduleName","入场检");
            put("module","inQuality");
        }});
        add(new HashMap<String,String>(){{
            put("moduleName","出厂检");
            put("module","outQuality");
        }});
        add(new HashMap<String,String>(){{
            put("moduleName","生产检");
            put("module","productionQuality");
        }});
    }}),
    INSTOCK("入库操作" ,"instock", new ArrayList<Map<String,String>>(){{
        add(new HashMap<String,String>(){{
            put("moduleName","生产入库");
            put("module","productionInstock");
        }});
        add(new HashMap<String,String>(){{
            put("moduleName","采购入库");
            put("module","purchaseInstock");
        }});
    }}),
    SHIP("工艺路线" ,"ship", new ArrayList<Map<String,String>>(){{

    }}),
    PURCHASE("采购" ,"purchase", new ArrayList<Map<String,String>>(){{
        add(new HashMap<String,String>(){{
            put("moduleName","采购申请");
            put("module","purchaseAsk");
        }});
        add(new HashMap<String,String>(){{
            put("moduleName","采购计划");
            put("module","purchasePlan");
        }});
        add(new HashMap<String,String>(){{
            put("moduleName","采购单");
            put("module","purchaseOrder");
        }});
    }});

//
//    @EnumValue
//    private final Map<String, String> detail;
    @EnumValue
    private List<Map<String, String>> list;


    @EnumValue
    private String name;
    @EnumValue
    private String type;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<Map<String, String>> getList() {
        return list;
    }




    ProcessType(String name,String type,List<Map<String,String>> list) {
        this.name = name;
        this.type = type;
        this.list = list;
    }


    @Override
    public String toString() {
        return "ProcessType{" +
                ", name=" + name +
                ", list=" + list +
                '}';
    }


    public static List<Map<String,Object>> enumList() {
        List<Map<String, Object>> enumList = new ArrayList<>();
        for (ProcessType value : ProcessType.values()) {
            Map<String,Object> enumDetail = new HashMap<>();
            List<Map<String,String>> list = new ArrayList<>(value.getList());
            String type = value.getType();
            String name = value.getName();
            enumDetail.put("details",list);
            enumDetail.put("name",name);
            enumDetail.put("type",type);
            enumList.add(enumDetail);
        }
        return enumList;
    }
}

