package cn.atsoft.dasheng.form.config;

import cn.atsoft.dasheng.form.model.*;
import cn.atsoft.dasheng.form.model.enums.FormFieldDataSourseType;
import cn.atsoft.dasheng.form.model.enums.FormFieldEnum;
import cn.atsoft.dasheng.form.model.result.FormFieldValue;
import cn.atsoft.dasheng.form.pojo.AuditType;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ProcessFormConfig implements ProcessInterface {

    @Override
    public List<FormField> getProcessForm(AuditType key) {

        switch (key) {
            case start:
                return new ArrayList<FormField>() {{
                    add(new FormField() {{
                        setType(FormFieldEnum.SelectUser.name());
                        setName("rules");
                        setKey("1");
                        setRequired(true);
                        setTitle(FormFieldEnum.SelectUser.getName());
                        setDataSource(new FormFieldDataSource() {{
                            setType(FormFieldDataSourseType.ApiData);
                        }});
                    }});
                }};
            case process:
                return new ArrayList<FormField>() {{
                    add(new FormField() {{
                        setType(FormFieldEnum.SelectUser.name());
                        setName("rules");
                        setKey("1");
                        setRequired(true);
                        setTitle(FormFieldEnum.SelectUser.getName());
                        setDataSource(new FormFieldDataSource() {{
                            setType(FormFieldDataSourseType.ApiData);
                        }});
                    }});

                    /**
                     * 状态
                     */
                    add(new FormField() {
                        {
                            setTitle("状态");
                            setKey("1");
                            setRequired(true);

                            setType(FormFieldEnum.Radio.name());
                            setName("action");
                            setDataSource(new FormFieldDataSource() {{
                                setValues(new ArrayList<FormFieldValue>() {{
                                    add(new FormFieldValue() {{
                                        setLabel("并签");
                                        setValue("1");
                                    }});
                                    add(new FormFieldValue() {{
                                        setLabel("或签");
                                        setValue("2");
                                    }});
                                }});
                            }});
                        }
                    });
                }};
            case send:
                return new ArrayList<FormField>() {{
                    add(new FormField() {{
                        setType(FormFieldEnum.SelectUser.name());
                        setName("rules");
                        setKey("1");
                        setRequired(true);
                        setTitle(FormFieldEnum.SelectUser.getName());
                        setDataSource(new FormFieldDataSource() {{
                            setType(FormFieldDataSourseType.ApiData);
                        }});
                    }});
                }};
        }
        return new ArrayList<>();
    }
}
