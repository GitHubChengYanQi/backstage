package cn.atsoft.dasheng.erp.service;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTask;
import cn.atsoft.dasheng.erp.model.params.QualityTaskParam;
import cn.atsoft.dasheng.erp.model.request.FormDataPojo;
import cn.atsoft.dasheng.erp.model.result.QualityTaskResult;
import cn.atsoft.dasheng.erp.model.result.TaskCount;
import cn.atsoft.dasheng.form.model.result.FormDataResult;
import com.baomidou.mybatisplus.extension.service.IService;

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

    void formDataFormat(FormDataResult param);

    //    @Override
    //    public void formDataFormat(FormDataResult param) {
    //        Long formId = param.getFormId();
    //        Inkind one = inkindService.lambdaQuery().eq(Inkind::getInkindId, formId).and(i -> i.eq(Inkind::getDisplay, 1)).one();
    //        param.setInkind(one);
    //        Long dataId = param.getDataId();
    //        List<FormDataValue> formDataValues = formDataValueService.lambdaQuery().eq(FormDataValue::getDataId, dataId).and(i -> i.eq(FormDataValue::getDisplay, 1)).list();
    //        List<Long> planIds = new ArrayList<>();
    //        for (FormDataValue formDataValue : formDataValues) {
    //            planIds.add(formDataValue.getField());
    //        }
    //        List<QualityPlanDetail> planDetails = planIds.size() == 0 ? new ArrayList<>() : qualityPlanDetailService.lambdaQuery().in(QualityPlanDetail::getPlanDetailId, planIds).and(i -> i.eq(QualityPlanDetail::getDisplay, 1)).list();
    //        List<Long> checkIds = new ArrayList<>();
    //        for (QualityPlanDetail planDetail : planDetails) {
    //            checkIds.add(planDetail.getQualityCheckId());
    //        }
    //
    //
    //        List<QualityCheck> qualityChecklist = checkIds.size() == 0 ? new ArrayList<>() : qualityCheckService.lambdaQuery().in(QualityCheck::getQualityCheckId, checkIds).eq(QualityCheck::getDisplay, 1).list();
    //        List<QualityCheckResult> qualityCheckResults = new ArrayList<>();
    //        for (QualityCheck qualityCheck : qualityChecklist) {
    //            QualityCheckResult qualityCheckResult = new QualityCheckResult();
    //            ToolUtil.copyProperties(qualityCheck, qualityCheckResult);
    //            qualityCheckResults.add(qualityCheckResult);
    //        }
    //        List<Map<String, Object>> maps = new ArrayList<>();
    //        for (FormDataValue formDataValue : formDataValues) {
    //            for (QualityPlanDetail planDetail : planDetails) {
    //                if (formDataValue.getField().equals(planDetail.getPlanDetailId())) {
    //                    for (QualityCheckResult qualityCheck : qualityCheckResults) {
    //                        if (qualityCheck.getQualityCheckId().equals(planDetail.getQualityCheckId())) {
    //                            Map<String, Object> map = new HashMap<>();
    //                            map.put("name", qualityCheck.getName());
    //                            map.put("value", formDataValue.getValue());
    //                            map.put("field", qualityCheck);
    //                            maps.add(map);
    //                        }
    //
    //                    }
    //                }
    //            }
    //
    //        }
    //        param.setValueResults(maps);
    //    }
    void formDataFormat1(List<FormDataResult> param);

    /**
     * 详情格式化
     *
     * @author
     * @Date 2021-11-16
     */
    void detailFormat(QualityTaskResult param);

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


    List<TaskCount>  backIkind(Long id);

}
