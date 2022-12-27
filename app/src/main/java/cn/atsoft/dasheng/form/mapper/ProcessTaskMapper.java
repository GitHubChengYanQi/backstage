package cn.atsoft.dasheng.form.mapper;

import cn.atsoft.dasheng.erp.model.params.DataStatisticsViewParam;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessTaskResult;
import cn.atsoft.dasheng.form.model.result.TaskViewResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程任务表	 Mapper 接口
 * </p>
 *
 * @author Jazz
 * @since 2021-11-19
 */
public interface ProcessTaskMapper extends BaseMapper<ActivitiProcessTask> {

  Page<TaskViewResult> taskNumberView(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
  Page<TaskViewResult> taskTypeView(@Param("page") Page page,@Param("paramCondition") DataStatisticsViewParam paramCondition);
  List<TaskViewResult> taskUserView(@Param("paramCondition") DataStatisticsViewParam paramCondition);
}
