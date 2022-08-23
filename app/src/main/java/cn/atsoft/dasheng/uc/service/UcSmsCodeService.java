package cn.atsoft.dasheng.uc.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.uc.entity.UcSmsCode;
import cn.atsoft.dasheng.uc.model.params.UcSmsCodeParam;
import cn.atsoft.dasheng.uc.model.result.UcSmsCodeResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  -  服务类
 * </p>
 *
 * @author Sing
 * @since 2021-03-16
 */
public interface UcSmsCodeService extends IService<UcSmsCode> {

    String getCode(String phone);

    Boolean sendCode(String phone,String Code);
    /**
     * 新增
     *
     * @author Sing
     * @Date 2021-03-16
     */
    void add(UcSmsCodeParam param);

    /**
     * 删除
     *
     * @author Sing
     * @Date 2021-03-16
     */
    void delete(UcSmsCodeParam param);

    /**
     * 更新
     *
     * @author Sing
     * @Date 2021-03-16
     */
    void update(UcSmsCodeParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-16
     */
    UcSmsCodeResult findBySpec(UcSmsCodeParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author Sing
     * @Date 2021-03-16
     */
    List<UcSmsCodeResult> findListBySpec(UcSmsCodeParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author Sing
     * @Date 2021-03-16
     */
     PageInfo findPageBySpec(UcSmsCodeParam param);

}
