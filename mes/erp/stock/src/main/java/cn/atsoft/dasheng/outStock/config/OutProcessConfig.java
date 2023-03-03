package cn.atsoft.dasheng.outStock.config;

import cn.atsoft.dasheng.form.model.ModelProcessDao;
import cn.atsoft.dasheng.form.model.ModelType;
import cn.atsoft.dasheng.form.model.ProcessStepTypeDao;
import cn.atsoft.dasheng.form.model.enums.ProcessStepTypeEnum;
import cn.atsoft.dasheng.form.pojo.ProcessModuleEnum;
import cn.atsoft.dasheng.form.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class OutProcessConfig {
    @Autowired
    private ModelService modelService;

    @Bean
    public void outStockRouteRegister() {
        modelService.register(new ModelProcessDao(){{
            setModel("OutStock");
            setModelName("出库单");
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
