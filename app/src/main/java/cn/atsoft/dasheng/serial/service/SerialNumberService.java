package cn.atsoft.dasheng.serial.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.serial.entity.SerialNumber;
import cn.atsoft.dasheng.serial.model.params.SerialNumberParam;
import cn.atsoft.dasheng.serial.model.result.SerialNumberResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流水号 服务类
 * </p>
 *
 * @author 
 * @since 2021-11-04
 */
public interface SerialNumberService extends IService<SerialNumber> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-11-04
     */
    String add(SerialNumberParam param);

    String genNumber();

    /**
     * 指定数量新增
     * @param param
     */
    List<String> addBatch(SerialNumberParam param);


    /**
     * 删除
     *
     * @author 
     * @Date 2021-11-04
     */
    void delete(SerialNumberParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-11-04
     */
    void update(SerialNumberParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-11-04
     */
    SerialNumberResult findBySpec(SerialNumberParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-11-04
     */
    List<SerialNumberResult> findListBySpec(SerialNumberParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-11-04
     */
     PageInfo<SerialNumberResult> findPageBySpec(SerialNumberParam param);

}
