package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.SpeechcraftTypeDetail;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftTypeDetailParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftTypeDetailResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 话术分类详细 服务类
 * </p>
 *
 * @author cheng
 * @since 2021-09-13
 */
public interface SpeechcraftTypeDetailService extends IService<SpeechcraftTypeDetail> {

    /**
     * 新增
     *
     * @author cheng
     * @Date 2021-09-13
     */
    void add(SpeechcraftTypeDetailParam param);

    /**
     * 删除
     *
     * @author cheng
     * @Date 2021-09-13
     */
    void delete(SpeechcraftTypeDetailParam param);

    /**
     * 更新
     *
     * @author cheng
     * @Date 2021-09-13
     */
    void update(SpeechcraftTypeDetailParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author cheng
     * @Date 2021-09-13
     */
    SpeechcraftTypeDetailResult findBySpec(SpeechcraftTypeDetailParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author cheng
     * @Date 2021-09-13
     */
    List<SpeechcraftTypeDetailResult> findListBySpec(SpeechcraftTypeDetailParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author cheng
     * @Date 2021-09-13
     */
     PageInfo<SpeechcraftTypeDetailResult> findPageBySpec(SpeechcraftTypeDetailParam param);

}
