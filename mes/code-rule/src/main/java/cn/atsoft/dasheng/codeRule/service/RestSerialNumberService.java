package cn.atsoft.dasheng.codeRule.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.codeRule.entity.RestSerialNumber;
import cn.atsoft.dasheng.codeRule.model.params.RestSerialNumberParam;
import cn.atsoft.dasheng.codeRule.model.result.RestSerialNumberResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 流水号 服务类
 * </p>
 *
 * @author 
 * @since 2021-11-04
 */
public interface RestSerialNumberService extends IService<RestSerialNumber> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-11-04
     */
    String add(RestSerialNumberParam param);

    /**
     * 指定数量新增
     * @param param
     */
    List<String> addBatch(RestSerialNumberParam param);


    /**
     * 删除
     *
     * @author 
     * @Date 2021-11-04
     */
    void delete(RestSerialNumberParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-11-04
     */
    void update(RestSerialNumberParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-11-04
     */
    RestSerialNumberResult findBySpec(RestSerialNumberParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-11-04
     */
    List<RestSerialNumberResult> findListBySpec(RestSerialNumberParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-11-04
     */
     PageInfo<RestSerialNumberResult> findPageBySpec(RestSerialNumberParam param);

}
