package cn.stylefeng.guns.sys.modular.rest.mapper;

import cn.stylefeng.guns.sys.modular.rest.entity.RestDictType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典类型表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-13
 */
public interface RestDictTypeMapper extends BaseMapper<RestDictType> {

    List<Map<String, Object>> dictTypeSelect(@Param("name") String name);

}
