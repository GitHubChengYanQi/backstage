package cn.atsoft.dasheng.form.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.Remarks;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.model.result.RemarksResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * log备注 服务类
 * </p>
 *
 * @author song
 * @since 2021-12-16
 */
public interface RemarksService extends IService<Remarks> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-12-16
     */
    void add(Long logId, String note);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-12-16
     */
    void delete(RemarksParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-12-16
     */
    void update(RemarksParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-12-16
     */
    RemarksResult findBySpec(RemarksParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-12-16
     */
    List<RemarksResult> findListBySpec(RemarksParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-12-16
     */
    PageInfo<RemarksResult> findPageBySpec(RemarksParam param);


    void addNote(Long taskId, String note);
}
