package cn.atsoft.dasheng.action;


import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.form.entity.DocumentsStatus;
import cn.atsoft.dasheng.form.service.DocumentsStatusService;


public interface FormActionInterface {

    DocumentsStatusService documentsStatusService = SpringContextHolder.getBean(DocumentsStatusService.class);

    /**
     * 通过动作获取状态码
     *
     * @param
     * @return
     */
    default Integer getStatus(String formType) {
        String name = this.toString();
        DocumentsStatus status = documentsStatusService.query().eq("form_type", formType).eq("action", name).one();
        return status.getFormStatus();
    }


    default void setStatus(int status, String formType) {
        String name = this.toString();
        DocumentsStatus documentsStatus = new DocumentsStatus();
        documentsStatus.setFormStatus(status);
        documentsStatus.setAction(name);
        documentsStatus.setFormType(formType);
        documentsStatusService.save(documentsStatus);

    }
    Integer getStatus();
}
