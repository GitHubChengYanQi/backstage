package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.request.FormValues;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.model.result.TaskCount;
import cn.atsoft.dasheng.erp.pojo.FormDataRequest;
import cn.atsoft.dasheng.erp.pojo.QualityTaskChild;
import cn.atsoft.dasheng.erp.pojo.TaskComplete;
import cn.atsoft.dasheng.form.model.result.FormDataResult;

import cn.atsoft.dasheng.orCode.BindParam;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 质检任务 服务类
 * </p>
 *
 * @author
 * @since 2021-11-16
 */
public interface QualityTaskService extends IService<QualityTask> {

    /**
     * 新增
     *
     * @author
     * @Date 2021-11-16
     */
    void add(QualityTaskParam param);


    void formDataFormat1(List<FormDataResult> param);

    /**
     * 详情格式化
     *
     * @author
     * @Date 2021-11-16
     */
    void detailFormat(QualityTaskResult result);

    /**
     * 删除
     *
     * @author
     * @Date 2021-11-16
     */
    void delete(QualityTaskParam param);

    /**
     * 更新
     *
     * @author
     * @Date 2021-11-16
     */
    void update(QualityTaskParam param);

    @Transactional
    void checkOver(QualityTaskParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author
     * @Date 2021-11-16
     */
    QualityTaskResult findBySpec(QualityTaskParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author
     * @Date 2021-11-16
     */
    List<QualityTaskResult> findListBySpec(QualityTaskParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author
     * @Date 2021-11-16
     */
    PageInfo<QualityTaskResult> findPageBySpec(QualityTaskParam param);

    /**
     * 添加formData
     *
     * @param formDataPojo
     */
    void addFormData(FormDataPojo formDataPojo);


    List<TaskCount> backIkind(Long id);

    /**
     * 添加子任务
     *
     * @param
     */
    void addChild(QualityTaskChild child);

    /**
     * 查询子任务
     *
     * @param id
     * @return
     */
    QualityTaskResult backChildTask(Long id);


    FormDataRequest valueResults(Long qrcodeId, String type);


    void updateDataValue(FormValues formValues);

    /**
     * 质检完成
     *
     * @param
     */
    void taskComplete(TaskComplete taskComplete);

    /**
     * 更新子任务
     *
     * @param taskId
     */
    void updateChildTask(Long taskId,Integer state);

    /**
     * 查询主任务和子任务
     *
     * @param id
     * @return
     */
    QualityTaskResult getTask(Long id);

    List<FormDataResult> getDetail(BindParam bindParam);

    List<FormDataResult> inStockDetail(BindParam bindParam);

}
