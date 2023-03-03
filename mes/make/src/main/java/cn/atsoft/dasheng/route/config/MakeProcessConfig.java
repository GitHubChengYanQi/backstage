package cn.atsoft.dasheng.route.config;

import cn.atsoft.dasheng.form.model.*;
import cn.atsoft.dasheng.form.model.enums.ModelEnum;
import cn.atsoft.dasheng.form.model.enums.ProcessStepTypeEnum;
import cn.atsoft.dasheng.form.pojo.ProcessModuleEnum;
import cn.atsoft.dasheng.form.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class MakeProcessConfig {
    @Autowired
    private ModelService modelService;

    @Bean
    public void makeRegister() {
        modelService.register(new ModelProcessDao(){{
            setModel("Ship");
            setModelName("工艺路线");
            setTypes(new ArrayList<ModelType>(){{
                add(new ModelType(){{
                    setName("ship");
                    setType("工艺路线");
                }});

            }});
            setStepTypes(new ArrayList<ProcessStepTypeDao>(){{
                add(new ProcessStepTypeDao(){{
                    setType(ProcessStepTypeEnum.single);
                    setTypeName("串行工序");
                    setKey("4");
                }});
                add(new ProcessStepTypeDao(){{
                    setType(ProcessStepTypeEnum.branch);
                    setTypeName("并行工序");
                    setKey("1");
                }});
            }});
        }});
    }
}
