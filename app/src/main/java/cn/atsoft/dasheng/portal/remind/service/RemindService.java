package cn.atsoft.dasheng.portal.remind.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.remind.entity.Remind;
import cn.atsoft.dasheng.portal.remind.model.params.JsonDataList;
import cn.atsoft.dasheng.portal.remind.model.params.RemindParam;
import cn.atsoft.dasheng.portal.remind.model.result.RemindResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 提醒表 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-08-26
 */
public interface RemindService extends IService<Remind> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-08-26
     */
    void add(RemindParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-08-26
     */
    void delete(RemindParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-08-26
     */
    void update(RemindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-26
     */
    RemindResult findBySpec(RemindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-08-26
     */
    List<RemindResult> findListBySpec(RemindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-08-26
     */
     PageInfo<RemindResult> findPageBySpec(RemindParam param);

}
