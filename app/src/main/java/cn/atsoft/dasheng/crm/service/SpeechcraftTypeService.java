package cn.atsoft.dasheng.crm.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.SpeechcraftType;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftTypeParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftTypeResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 话术分类 服务类
 * </p>
 *
 * @author 
 * @since 2021-09-13
 */
public interface SpeechcraftTypeService extends IService<SpeechcraftType> {

    /**
     * 新增
     *
     * @author 
     * @Date 2021-09-13
     */
    void add(SpeechcraftTypeParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2021-09-13
     */
    void delete(SpeechcraftTypeParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2021-09-13
     */
    void update(SpeechcraftTypeParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2021-09-13
     */
    SpeechcraftTypeResult findBySpec(SpeechcraftTypeParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2021-09-13
     */
    List<SpeechcraftTypeResult> findListBySpec(SpeechcraftTypeParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2021-09-13
     */
     PageInfo<SpeechcraftTypeResult> findPageBySpec(SpeechcraftTypeParam param);

}
