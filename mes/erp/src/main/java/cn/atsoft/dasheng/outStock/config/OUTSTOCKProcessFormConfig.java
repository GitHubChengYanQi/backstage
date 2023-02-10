package cn.atsoft.dasheng.outStock.config;

import cn.atsoft.dasheng.form.model.*;
import cn.atsoft.dasheng.form.model.enums.FormFieldDataSourseType;
import cn.atsoft.dasheng.form.model.result.FormFieldLink;
import cn.atsoft.dasheng.form.model.result.FormFieldValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static cn.atsoft.dasheng.form.model.FormFieldEnum.StepsType;

@Service
public class OUTSTOCKProcessFormConfig implements ProcessInterface {
    @Override
    public List<FormField> getProcessForm() {
        return new ArrayList<FormField>() {{

            /**
             *  审批
             */
            add(new FormField() {{
                setTitle(StepsType.getName());
                setName(StepsType.getName());
                setType(FormFieldEnum.Radio);
                setField("type");
                setDataSource(new FormFieldDataSource(){{
                    setType(FormFieldDataSourseType.FormData);
                    setTitle("审批");

                    setValues(new ArrayList<FormFieldValue>(){{
                        add(new FormFieldValue(){{
                            setValue("audit");
                            setLabel("审批");
                        }});
                        add(new FormFieldValue(){{
                            setValue("status");
                            setLabel("状态");
                        }});
                    }});
                }});
            }});
            /**
             * 人员范围
             */
            add(new FormField() {{
                setType(FormFieldEnum.SelectUser);
                setName(FormFieldEnum.SelectUser.getName());
                setField("rules");
                setDataSource(new FormFieldDataSource(){{
                    setType(FormFieldDataSourseType.FormData);
                }});
            }});

            /**
             * 单据状态
             */
            add(new FormField() {{
                setName("状态名称");
                setType(FormFieldEnum.Input);
                setField("stepsName");
                setLinks(new ArrayList<FormFieldLink>(){{
                    add(new FormFieldLink(){{
                        setTitle(StepsType.getName());
                        setKey("type");
                        setValue("status");
                    }});
                }});
            }});
            add(new FormField() {{
                setName("单据状态");
                setType(FormFieldEnum.Select);
                setField("documentsStatusId");
                setDataSource(new FormFieldDataSource() {{
                    setApiUrl("documentStatus/getDetails");
                }});

            }});
            add(new FormField() {{
                setName("单据动作");
                setType(FormFieldEnum.Select);
                setField("actionStatuses");
                setDataSource(new FormFieldDataSource() {{
                    setApiUrl("");
                }});

            }});
            /**
             * 状态
             */
            add(new FormField() {{
                setName("状态");
                setType(FormFieldEnum.Radio);
                setField("action");
                setDataSource(new FormFieldDataSource(){{
                    setValues(new ArrayList<FormFieldValue>(){{
                        add(new FormFieldValue(){{
                            setLabel("并签");
                            setValue("1");
                        }});
                        add(new FormFieldValue(){{
                            setLabel("或签");
                            setValue("2");
                        }});
                    }});
                }});
            }});

        }};
    }
}
