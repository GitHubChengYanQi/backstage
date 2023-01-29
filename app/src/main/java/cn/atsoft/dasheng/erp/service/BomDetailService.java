package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.BomDetail;
import cn.atsoft.dasheng.erp.model.params.BomDetailParam;
import cn.atsoft.dasheng.erp.model.params.BomParam;
import cn.atsoft.dasheng.erp.model.result.BomDetailResult;
import cn.atsoft.dasheng.erp.model.result.BomResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BomDetailService extends IService<BomDetail> {
    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-10-26
     */
    void add(BomDetailParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-10-26
     */
    void delete(BomDetailParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-10-26
     */
    void update(BomDetailParam param);
    /**
     * 查询单条数据，Specification模式
     *
     */
    BomDetailResult findBySpec(BomDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     */
    List<BomDetailResult> findListBySpec(BomDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     */
    PageInfo<BomDetailResult> findPageBySpec(BomDetailParam param);
}
