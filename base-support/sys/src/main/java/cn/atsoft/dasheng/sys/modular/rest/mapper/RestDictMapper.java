package cn.atsoft.dasheng.sys.modular.rest.mapper;

import cn.atsoft.dasheng.base.pojo.node.ZTreeNode;
import cn.atsoft.dasheng.sys.modular.rest.entity.RestDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 基础字典 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-13
 */
public interface RestDictMapper extends BaseMapper<RestDict> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> dictTree(@Param("dictTypeId") Long dictTypeId);

    /**
     * where parentIds like ''
     */
    List<RestDict> likeParentIds(@Param("dictId") Long dictId);

    List<RestDict> radio(@Param("dictTypeId") Long dictTypeId);
}
