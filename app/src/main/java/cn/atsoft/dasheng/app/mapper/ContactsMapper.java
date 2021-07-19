package cn.atsoft.dasheng.app.mapper;

import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.app.model.params.ContactsParam;
import cn.atsoft.dasheng.app.model.result.ContactsResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 联系人表 Mapper 接口
 * </p>
 *
 * @author ta
 * @since 2021-07-19
 */
public interface ContactsMapper extends BaseMapper<Contacts> {

    /**
     * 获取列表
     *
     * @author ta
     * @Date 2021-07-19
     */
    List<ContactsResult> customList(@Param("paramCondition") ContactsParam paramCondition);

    /**
     * 获取map列表
     *
     * @author ta
     * @Date 2021-07-19
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") ContactsParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author ta
     * @Date 2021-07-19
     */
    Page<ContactsResult> customPageList(@Param("page") Page page, @Param("paramCondition") ContactsParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author ta
     * @Date 2021-07-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ContactsParam paramCondition);

}
