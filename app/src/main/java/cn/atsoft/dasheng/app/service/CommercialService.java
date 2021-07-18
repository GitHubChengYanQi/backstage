package cn.atsoft.dasheng.app.service;

import cn.atsoft.dasheng.app.entity.Commercial;
import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.app.model.params.CommercialParam;
import cn.atsoft.dasheng.app.model.result.AdressResult;
import cn.atsoft.dasheng.app.model.result.CommercialResult;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommercialService extends IService<Commercial> {
  /**
   * 新增
   *
   * @author
   * @Date 2021-07-16
   */
  void add(CommercialParam param);

  /**
   * 删除
   *
   * @author
   * @Date 2021-07-16
   */
  void delete(CommercialParam param);

  /**
   * 更新
   *
   * @author
   * @Date 2021-07-16
   */
  void update(CommercialParam param);

  /**
   * 查询单条数据，Specification模式
   *
   * @author
   * @Date 2021-07-16
   */
  CommercialResult findBySpec(CommercialParam param);

  /**
   * 查询列表，Specification模式
   *
   * @author
   * @Date 2021-07-16
   */
  List<CommercialResult> findListBySpec(CommercialParam param);

  /**
   * 查询分页数据，Specification模式
   *
   * @author
   * @Date 2021-07-16
   */
  PageInfo<CommercialResult> findPageBySpec(CommercialParam param);
}
