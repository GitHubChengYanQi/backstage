package cn.atsoft.dasheng.portal.companyaddress.mapper;

import cn.atsoft.dasheng.portal.companyaddress.entity.CompanyAddress;
import cn.atsoft.dasheng.portal.companyaddress.model.params.CompanyAddressParam;
import cn.atsoft.dasheng.portal.companyaddress.model.result.CompanyAddressResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报修 Mapper 接口
 * </p>
 *
 * @author siqiang
 * @since 2021-08-20
 */
public interface CompanyAddressMapper extends BaseMapper<CompanyAddress> {

    /**
     * 获取列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    List<CompanyAddressResult> customList(@Param("paramCondition") CompanyAddressParam paramCondition);

    /**
     * 获取map列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") CompanyAddressParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    Page<CompanyAddressResult> customPageList(@Param("page") Page page, @Param("paramCondition") CompanyAddressParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CompanyAddressParam paramCondition);

}
