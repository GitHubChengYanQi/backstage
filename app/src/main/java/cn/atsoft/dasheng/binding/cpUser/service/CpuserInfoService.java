package cn.atsoft.dasheng.binding.cpUser.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.binding.cpUser.entity.CpuserInfo;
import cn.atsoft.dasheng.binding.cpUser.model.params.CpuserInfoParam;
import cn.atsoft.dasheng.binding.cpUser.model.result.CpuserInfoResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2021-10-12
 */
public interface CpuserInfoService extends IService<CpuserInfo> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-10-12
     */
    void add(CpuserInfoParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-10-12
     */
    void delete(CpuserInfoParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-10-12
     */
    void update(CpuserInfoParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-10-12
     */
    CpuserInfoResult findBySpec(CpuserInfoParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-10-12
     */
    List<CpuserInfoResult> findListBySpec(CpuserInfoParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-10-12
     */
     PageInfo<CpuserInfoResult> findPageBySpec(CpuserInfoParam param);

}
