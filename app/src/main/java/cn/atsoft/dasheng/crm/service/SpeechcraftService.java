package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Speechcraft;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 话术基础资料 服务类
 * </p>
 *
 * @author 
 * @since 2021-09-11
 */
public interface SpeechcraftService extends IService<Speechcraft> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-11
     */
    void add(SpeechcraftParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-09-11
     */
    void delete(SpeechcraftParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-09-11
     */
    void update(SpeechcraftParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-11
     */
    SpeechcraftResult findBySpec(SpeechcraftParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-11
     */
    List<SpeechcraftResult> findListBySpec(SpeechcraftParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-11
     */
     PageInfo<SpeechcraftResult> findPageBySpec(SpeechcraftParam param);

}
