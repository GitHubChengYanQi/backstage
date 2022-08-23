package cn.atsoft.dasheng.api.uc.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.api.uc.entity.OpenUserInfo;
import cn.atsoft.dasheng.api.uc.model.params.OpenUserInfoParam;
import cn.atsoft.dasheng.api.uc.model.result.OpenUserInfoResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2021-08-25
 */
public interface OpenUserInfoService extends IService<OpenUserInfo> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-08-25
     */
    void add(OpenUserInfoParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-08-25
     */
    void delete(OpenUserInfoParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-08-25
     */
    void update(OpenUserInfoParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-08-25
     */
    OpenUserInfoResult findBySpec(OpenUserInfoParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-08-25
     */
    List<OpenUserInfoResult> findListBySpec(OpenUserInfoParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-08-25
     */
     PageInfo findPageBySpec(OpenUserInfoParam param);

}
