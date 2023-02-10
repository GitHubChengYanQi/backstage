package cn.atsoft.dasheng.outStock.config;

import cn.atsoft.dasheng.form.model.ModelProcessDao;
import cn.atsoft.dasheng.form.model.ModelType;
import cn.atsoft.dasheng.form.model.ProcessStepTypeDao;
import cn.atsoft.dasheng.form.model.enums.ModelEnum;
import cn.atsoft.dasheng.form.model.enums.ProcessStepTypeEnum;
import cn.atsoft.dasheng.form.pojo.ProcessModuleEnum;
import cn.atsoft.dasheng.form.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Configuration
public class ProcessConfig {
    @Autowired
    private ModelService modelService;

    @Bean
    public void register() {
        modelService.register(new ModelProcessDao(){{
            setModel(ModelEnum.OUTSTOCK);
            setModelName(ModelEnum.OUTSTOCK.getName());
            setTypes(new ArrayList<ModelType>(){{
                add(new ModelType(){{
                    setName(ProcessModuleEnum.productionOutStock.getModuleName());
                    setType(ProcessModuleEnum.productionOutStock.name().toString());
                }});
                add(new ModelType(){{
                    setName(ProcessModuleEnum.pickLists.getModuleName());
                    setType(ProcessModuleEnum.pickLists.name().toString());
                }});
            }});
            setStepTypes(new ArrayList<ProcessStepTypeDao>(){{
                add(new ProcessStepTypeDao(){{
                    setType(ProcessStepTypeEnum.branch);
                    setTypeName("分支");
                    setKey("4");
                }});
                add(new ProcessStepTypeDao(){{
                    setType(ProcessStepTypeEnum.single);
                    setTypeName("审批");
                    setKey("1");
                }});
                add(new ProcessStepTypeDao(){{
                    setType(ProcessStepTypeEnum.single);
                    setTypeName("抄送");
                    setKey("2");
                }});
            }});
        }});
    }
}
