package cn.atsoft.dasheng.form.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.mapper.ActivitiProcessMapper;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;

import cn.atsoft.dasheng.form.pojo.ProcessEnum;
import cn.atsoft.dasheng.form.pojo.ProcessModuleEnum;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.service.ActivitiStepsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 流程主表 服务实现类
 * </p>
 *
 * @author Sing
 * @since 2021-11-10
 */
@Service
public class ActivitiProcessServiceImpl extends ServiceImpl<ActivitiProcessMapper, ActivitiProcess> implements ActivitiProcessService {
    @Autowired
    private ActivitiStepsService activitiStepsService;

    @Override
    public void add(ActivitiProcessParam param) {
        ActivitiProcess process = this.query().eq("process_name", param.getProcessName()).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(process)) {
            throw new ServiceException(500, "名字以重复");
        }
        ActivitiProcess entity = getEntity(param);

        this.save(entity);
    }

    @Override
    public List<String> getType() {
        List<String> type = new ArrayList<>();
        ProcessEnum[] values = ProcessEnum.values();

        for (ProcessEnum value : values) {
            type.add(value.getValue());
        }
        return type;
    }

    @Override
    public void delete(ActivitiProcessParam param) {
        ActivitiProcess activitiProcess = new ActivitiProcess();
        activitiProcess.setDisplay(0);
        QueryWrapper<ActivitiProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_id", param.getProcessId());
        this.update(activitiProcess, queryWrapper);
    }

    @Override
    public ActivitiProcess getByFromId(Long fromId) {
        return this.getOne(new QueryWrapper<ActivitiProcess>() {{
            eq("from_id", fromId);
        }});
    }

    @Override
    @Transactional
    public void update(ActivitiProcessParam param) {
        Integer module = this.query().eq("module", param.getModule()).count();
        if (module == 1) {
            ActivitiProcess process = this.getById(param.getProcessId());
            if (process.getStatus() == 99) {
                if (!process.getStatus().equals(param.getStatus())) {
                    throw new ServiceException(500, "不可全部停用");
                }
                ActivitiProcess oldEntity = getOldEntity(param);
                ActivitiProcess newEntity = getEntity(param);
                ToolUtil.copyProperties(newEntity, oldEntity);
                this.updateById(newEntity);
                return;
            }
        }

        //判断启用是否配置
        if (param.getStatus() == 98) {
            Integer stepsCount = activitiStepsService.query().eq("process_id", param.getProcessId()).count();
            if (stepsCount == 0) {
                throw new ServiceException(500, "请先设置流程");
            }
            //确保有流程启用
            Integer count = this.query().eq("module", param.getModule()).count();
            if (count > 1) {
                ActivitiProcess process = this.query().eq("module", param.getModule()).eq("status", 99)
                        .ne("process_id", param.getProcessId())
                        .one();
                if (ToolUtil.isEmpty(process)) {
                    throw new ServiceException(500, "必须有一个启用的流程");
                }
            }
        }
        //当前流程设为启用 其他流程设为停用
        if (param.getStatus() == 99) {
            ActivitiProcess process = this.query().eq("module", param.getModule())
                    .eq("status", 99)
                    .eq("display", 1)
                    .one();
            if (ToolUtil.isNotEmpty(process)) {
                process.setStatus(98);
                this.updateById(process);
            }
        }

        ActivitiProcess oldEntity = getOldEntity(param);
        ActivitiProcess newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);


    }

    @Override
    public ActivitiProcessResult findBySpec(ActivitiProcessParam param) {
        return null;
    }

    @Override
    public List<ActivitiProcessResult> findListBySpec(ActivitiProcessParam param) {
        return null;
    }

    @Override
    public PageInfo<ActivitiProcessResult> findShipPageBySpec(ActivitiProcessParam param) {
        Page<ActivitiProcessResult> pageContext = getPageContext();
        IPage<ActivitiProcessResult> page = this.baseMapper.shipPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo<ActivitiProcessResult> findPageBySpec(ActivitiProcessParam param) {

        Page<ActivitiProcessResult> pageContext = getPageContext();
        IPage<ActivitiProcessResult> page = this.baseMapper.customPageList(pageContext, param);

        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ActivitiProcessParam param) {
        return param.getProcessId();
    }

    private Page<ActivitiProcessResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ActivitiProcess getOldEntity(ActivitiProcessParam param) {
        return this.getById(getKey(param));
    }

    private ActivitiProcess getEntity(ActivitiProcessParam param) {
        ActivitiProcess entity = new ActivitiProcess();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }




    @Override
    public List<String> getModule(ProcessEnum processEnum) {
        if (ToolUtil.isEmpty(processEnum)) {
            throw new ServiceException(500, "请选择类型");
        }
        List<String> module = new ArrayList<>();
        switch (processEnum) {
            case 询价:

            case 质检:
                module.add(ProcessModuleEnum.入厂检.getValue());
                break;
            case 采购:
                module.add(ProcessModuleEnum.采购申请.getValue());
                module.add(ProcessModuleEnum.采购计划.getValue());
                break;
        }
        return module;
    }
}
