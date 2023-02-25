package cn.atsoft.dasheng.route.config;

import cn.atsoft.dasheng.form.model.FormField;
import cn.atsoft.dasheng.form.model.FormFieldDataSource;
import cn.atsoft.dasheng.form.model.ProcessInterface;
import cn.atsoft.dasheng.form.model.enums.FormFieldEnum;
import cn.atsoft.dasheng.form.model.result.FormFieldLink;
import cn.atsoft.dasheng.form.model.result.FormFieldValue;
import cn.atsoft.dasheng.form.pojo.AuditType;
import cn.atsoft.dasheng.route.model.enums.MakeFormFieldEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("ShipProcessFormConfig")
public class ShipProcessFormConfig implements ProcessInterface {

    @Override
    public List<FormField> getProcessForm(AuditType key) {
        switch (key){
            case start:
                return new ArrayList<FormField>() {
                    {
                        add(new FormField() {{
                            setTitle(MakeFormFieldEnum.RouteSkuSelect.getName());
                            setName("skuId");
                            setType(MakeFormFieldEnum.RouteSkuSelect.name());
                            setDataSource(new FormFieldDataSource() {{
                                setTitle("适用物料");
                                setApiUrl("/sku/list");
                            }});
                        }});
                    }};
            case process:
                return new ArrayList<FormField>() {{

                    /**
                     *  审批
                     */
                    add(new FormField() {{
                        setTitle(FormFieldEnum.StepsType.getName());
                        setName("type");
                        setType(FormFieldEnum.Radio.name());
                        setDataSource(new FormFieldDataSource(){{
                            setTitle("配置生产过程");

                            setValues(new ArrayList<FormFieldValue>(){{
                                add(new FormFieldValue(){{
                                    setValue("steps");
                                    setLabel("工序");
                                }});
                                add(new FormFieldValue(){{
                                    setValue("route");
                                    setLabel("工艺路线");
                                }});
                            }});
                        }});
                    }});

                    /**
                     * 工序名称
                     */
                    add(new FormField() {{
                        setTitle("工序名称");
                        setName("stepsName");
                        setType(FormFieldEnum.Select.name());
                        setDataSource(new FormFieldDataSource(){{
                            setApiUrl("documentStatus/getDetails");

                        }});
                        setLinks(new ArrayList<FormFieldLink>(){{
                            add(new FormFieldLink(){{
                                setTitle(FormFieldEnum.StepsType.getName());
                                setField("type");
                                setValue("steps");
                            }});
                        }});
                    }});
                    add(new FormField() {{
                        setTitle("投入与产出是否相同");
                        setName("productionType");
                        setType(FormFieldEnum.Radio.name());
                        setDataSource(new FormFieldDataSource() {{
                            setValues(new ArrayList<FormFieldValue>(){{
                                add(new FormFieldValue(){{
                                    setValue("in");
                                    setLabel("是");
                                }});
                                add(new FormFieldValue(){{
                                    setValue("out");
                                    setLabel("否");
                                }});
                            }});
                        }});

                    }});
                    add(new FormField() {{
                        setTitle("");
                        setType(MakeFormFieldEnum.RouteSkuSelect.name());
                        setName("sku");
                        setDataSource(new FormFieldDataSource() {{
                            setApiUrl("");
                        }});

                    }});
                    add(new FormField() {{
                        setTitle("投入物料信息");
                        setType(MakeFormFieldEnum.RouteInputSkuShow.name());
                        setName("sku");
                        setDataSource(new FormFieldDataSource() {{
                            setApiUrl("");
                        }});

                    }});
                    add(new FormField() {{
                        setTitle("关联信息");
                        setType(MakeFormFieldEnum.RouteAbout.name());
                        setName("about");
                        setDataSource(new FormFieldDataSource() {{
                            setApiUrl("");
                        }});

                    }});
                    add(new FormField() {{
                        setTitle("产出物料");
                        setType(FormFieldEnum.Select.name());
                        setName("sku");
                        setDataSource(new FormFieldDataSource() {{
                            setApiUrl("/sku/list");
                        }});
                        setLinks(new ArrayList<FormFieldLink>(){{
                            add(new FormFieldLink(){{
                                setField("type");
                                setValue("route");
                            }});
                        }});
                    }});
                    add(new FormField() {{
                        setTitle("产出数量");
                        setType(FormFieldEnum.InputNumber.name());
                        setName("number");
                        setDataSource(new FormFieldDataSource() {{
                        }});
                        setLinks(new ArrayList<FormFieldLink>(){{
                            add(new FormFieldLink(){{
                                setField("type");
                                setValue("route");
                            }});
                        }});
                    }});
                    add(new FormField() {{
                        setTitle("工序备注");
                        setType(FormFieldEnum.Input.name());
                        setName("note");
                        setDataSource(new FormFieldDataSource() {{
                        }});
                        setLinks(new ArrayList<FormFieldLink>(){{
                            add(new FormFieldLink(){{
                                setField("type");
                                setValue("route");
                            }});
                        }});
                    }});
                }};
            case branch:

        }
        return new ArrayList<>();
    }
}
