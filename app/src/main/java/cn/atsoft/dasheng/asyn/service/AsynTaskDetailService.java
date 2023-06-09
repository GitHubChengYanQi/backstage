package cn.atsoft.dasheng.asyn.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.asyn.entity.AsynTaskDetail;
import cn.atsoft.dasheng.asyn.model.params.AsynTaskDetailParam;
import cn.atsoft.dasheng.asyn.model.result.AsynTaskDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任务详情表 服务类
 * </p>
 *
 * @author song
 * @since 2022-04-26
 */
public interface AsynTaskDetailService extends IService<AsynTaskDetail> {

    /**
     * 新增
     *
     * @author song
     * @Date 2022-04-26
     */
    void add(AsynTaskDetailParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2022-04-26
     */
    void delete(AsynTaskDetailParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2022-04-26
     */
    void update(AsynTaskDetailParam param);



    Map<String, Integer> getNum(Long taskId);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2022-04-26
     */
    AsynTaskDetailResult findBySpec(AsynTaskDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2022-04-26
     */
    List<AsynTaskDetailResult> findListBySpec(AsynTaskDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2022-04-26
     */
     PageInfo<AsynTaskDetailResult> findPageBySpec(AsynTaskDetailParam param);

}
