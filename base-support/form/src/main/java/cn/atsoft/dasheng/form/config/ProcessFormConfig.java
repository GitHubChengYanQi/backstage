package cn.atsoft.dasheng.form.config;

import cn.atsoft.dasheng.form.model.*;
import cn.atsoft.dasheng.form.model.enums.FormFieldDataSourseType;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ProcessFormConfig implements ProcessInterface {

    @Override
    public List<FormField> getProcessForm() {
        return new ArrayList<FormField>() {{
            add(new FormField() {{
                setType(FormFieldEnum.SelectUser);
                setName(FormFieldEnum.SelectUser.getName());
                setTitle(FormFieldEnum.SelectUser.getName());
                setDataSource(new FormFieldDataSource(){{
                    setType(FormFieldDataSourseType.ApiData);
                }});
            }});
            add(new FormField() {{
                setType(FormFieldEnum.AuditOperation);
                setName(FormFieldEnum.AuditOperation.getName());
                setTitle(FormFieldEnum.SelectUser.getName());
                setDataSource(new FormFieldDataSource(){{
                    setType(FormFieldDataSourseType.ApiData);
                }});
            }});
        }};
    }
}
