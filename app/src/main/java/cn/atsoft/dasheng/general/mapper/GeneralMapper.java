package cn.atsoft.dasheng.general.mapper;


import cn.atsoft.dasheng.general.model.result.BomListResult;
import cn.atsoft.dasheng.general.model.result.ClassListResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GeneralMapper{
    /**
     * 通过模糊查询keyword 找到spuClassName包含 keyword 对应的数据
     * @param keyWord
     * @return
     */
    List<ClassListResult> customList(@Param("keyWord") String keyWord);

    /**
     * 通过模糊查询keyword 找到skuName,spuName,standard，model 包含 keyword 对应的数据
     */
    List<BomListResult> customListBySkuName(@Param("keyWord") String keyWord);

}
