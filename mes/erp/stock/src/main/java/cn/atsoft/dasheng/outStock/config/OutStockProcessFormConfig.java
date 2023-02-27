package cn.atsoft.dasheng.outStock.config;

import cn.atsoft.dasheng.form.model.*;
import cn.atsoft.dasheng.form.model.enums.FormFieldDataSourseType;
import cn.atsoft.dasheng.form.model.enums.FormFieldEnum;
import cn.atsoft.dasheng.form.model.result.FormFieldLink;
import cn.atsoft.dasheng.form.model.result.FormFieldValue;
import cn.atsoft.dasheng.form.pojo.AuditType;
import cn.atsoft.dasheng.outStock.model.enums.OutStockFormFieldEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static cn.atsoft.dasheng.form.model.enums.FormFieldEnum.StepsType;

@Service("OutStockProcessFormConfig")
public class OutStockProcessFormConfig implements ProcessInterface {
    @Override
    public List<FormField> getProcessForm(AuditType key) {
        switch (key) {
            case AuditType.start:
                return new ArrayList<FormField>() {{
                    /**
                     * 人员范围
                     */
                    add(new FormField() {{
                        setTitle(FormFieldEnum.SelectUser.getName());
                        setType(FormFieldEnum.SelectUser.name());
                        setName("rules");
                        setKey("1");
                        setRequired(true);
                        setDataSource(new FormFieldDataSource() {{
                            setType(FormFieldDataSourseType.FormData);
                        }});
                    }});






                }};
            case AuditType.route:
                    return new ArrayList<>();
            case AuditType.process:
                return new ArrayList<FormField>() {{

                    /**
                     *  审批
                     */
                    add(new FormField() {{
                        setTitle(FormFieldEnum.StepsType.getName());
                        setName("type");
                        setType(FormFieldEnum.Radio.name());
                        setKey("1");
                        setRequired(true);

                        setDataSource(new FormFieldDataSource() {{
                            setType(FormFieldDataSourseType.FormData);
                            setTitle("审批");

                            setValues(new ArrayList<FormFieldValue>() {{
                                add(new FormFieldValue() {{
                                    setValue("audit");
                                    setLabel("审批");
                                }});
                                add(new FormFieldValue() {{
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
                        setTitle(FormFieldEnum.SelectUser.getName());
                        setType(FormFieldEnum.SelectUser.name());
                        setName("rules");
                        setKey("1");
                        setRequired(true);

                        setDataSource(new FormFieldDataSource() {{
                            setType(FormFieldDataSourseType.FormData);
                        }});
                        setLinks(new ArrayList<FormFieldLink>(){{
                            add(new FormFieldLink() {{
                                setTitle(FormFieldEnum.StepsType.getName());
                                setField("type");
                                setValue("status");
                            }});
                            add(new FormFieldLink() {{
                                setTitle(FormFieldEnum.StepsType.getName());
                                setField("type");
                                setValue("audit");
                            }});
                        }});

                    }});

                    /**
                     * 单据状态
                     */
                    add(new FormField() {{
                        setTitle(OutStockFormFieldEnum.orderStatus.getName());
                        setName("documentsStatus");
                        setKey("1");
                        setRequired(true);

                        setType(OutStockFormFieldEnum.orderStatus.name());
                        setLinks(new ArrayList<FormFieldLink>() {{
                            setDataSource(new FormFieldDataSource() {{
                                setApiUrl("/documentStatus/getDetails");
                            }});
                            add(new FormFieldLink() {{
                                setTitle(FormFieldEnum.StepsType.getName());
                                setField("type");
                                setValue("status");
                            }});

                        }});
                    }});
                    add(new FormField() {{
                        setTitle("状态名称");
                        setName("stepsName");
                        setKey("1");

                        setType(FormFieldEnum.Input.name());


                    }});
//                    add(new FormField() {{
//                        setTitle("单据动作");
//                        setKey("1");
//
//                        setType(FormFieldEnum.Select);
//                        setName("actionStatuses");
//                        setDataSource(new FormFieldDataSource() {{
//                            setApiUrl("");
//                        }});
//
//                    }});
                    /**
                     * 状态
                     */
                    add(new FormField() {{
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
                        setLinks(new ArrayList<FormFieldLink>(){{
                            add(new FormFieldLink() {{
                                setTitle(FormFieldEnum.StepsType.getName());
                                setField("type");
                                setValue("status");
                            }});
                            add(new FormFieldLink() {{
                                setTitle(FormFieldEnum.StepsType.getName());
                                setField("type");
                                setValue("audit");
                            }});
                        }});
                    }});

                }};
        }
        return new ArrayList<>();
    }
}
