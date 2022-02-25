package cn.atsoft.dasheng.crm.mapper;

import cn.atsoft.dasheng.crm.entity.Bank;
import cn.atsoft.dasheng.crm.model.params.BankParam;
import cn.atsoft.dasheng.crm.model.result.BankResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-02-24
 */
public interface BankMapper extends BaseMapper<Bank> {

    /**
     * 获取列表
     *
     * @author song
     * @Date 2022-02-24
     */
    List<BankResult> customList(@Param("paramCondition") BankParam paramCondition);

    /**
     * 获取map列表
     *
     * @author song
     * @Date 2022-02-24
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") BankParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author song
     * @Date 2022-02-24
     */
    Page<BankResult> customPageList(@Param("page") Page page, @Param("paramCondition") BankParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author song
     * @Date 2022-02-24
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") BankParam paramCondition);

}
