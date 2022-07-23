package cn.atsoft.dasheng.orCode.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.orCode.BindParam;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeBindResult;
import com.baomidou.mybatisplus.extension.service.IService;
import sun.util.resources.cldr.mg.LocaleNames_mg;

import java.util.List;

/**
 * <p>
 * 二维码绑定 服务类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
public interface OrCodeBindService extends IService<OrCodeBind> {

    /**
     * 新增
     *
     * @author song
     * @Date 2021-10-29
     */
    OrCodeBind add(OrCodeBindParam param);

    /**
     * 删除
     *
     * @author song
     * @Date 2021-10-29
     */
    void delete(OrCodeBindParam param);

    /**
     * 更新
     *
     * @author song
     * @Date 2021-10-29
     */
    void update(OrCodeBindParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    OrCodeBindResult findBySpec(OrCodeBindParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    List<OrCodeBindResult> findListBySpec(OrCodeBindParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author song
     * @Date 2021-10-29
     */
    PageInfo<OrCodeBindResult> findPageBySpec(OrCodeBindParam param);

    /**
     * 返回datavalue
     *
     * @return
     */
    List<Long> backValue(List<Long> qrcodeIds);

    /**
     * 返回二维码
     *
     * @param formId
     * @return
     */
    Long getQrcodeId(Long formId);

    /**
     * 通过二维码扫描 formId
     *
     * @param qrcodeId
     * @return
     */
    Long getFormId(Long qrcodeId);

    /**
     * 通过多个二维码获取 formId
     *
     * @param qrcodeIds
     * @return
     */
    List<Long> getFormIds(List<Long> qrcodeIds);

    List<Long> getCodeIds(List<Long> fromIds);

}
