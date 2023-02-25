package cn.atsoft.dasheng.bom.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.bom.entity.RestBomDetail;
import cn.atsoft.dasheng.bom.model.params.RestBomDetailParam;
import cn.atsoft.dasheng.bom.model.result.RestBomDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RestBomDetailService extends IService<RestBomDetail> {
    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-10-26
     */
    void add(RestBomDetailParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-10-26
     */
    void delete(RestBomDetailParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-10-26
     */
    void update(RestBomDetailParam param);
    /**
     * 查询单条数据，Specification模式
     *
     */
    RestBomDetailResult findBySpec(RestBomDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     */
    List<RestBomDetailResult> findListBySpec(RestBomDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     */
    PageInfo<RestBomDetailResult> findPageBySpec(RestBomDetailParam param);
}
