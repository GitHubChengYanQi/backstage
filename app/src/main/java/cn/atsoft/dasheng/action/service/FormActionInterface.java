package cn.atsoft.dasheng.action.service;


import cn.atsoft.dasheng.core.util.SpringContextHolder;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.entity.DocumentsStatus;
import cn.atsoft.dasheng.form.service.DocumentStatusService;
import cn.atsoft.dasheng.form.service.DocumentsActionService;


public interface FormActionInterface {

    DocumentsActionService actionService = SpringContextHolder.getBean(DocumentsActionService.class);

    /**
     * 通过动作获取状态码
     *
     * @param
     * @return
     */
    default Long getStatus() {
        String name = this.toString();
        DocumentsAction action = actionService.query().eq("name", name).one();
        return action.getDocumentsStatusId();
    }


    default void setStatus(Long status, String fromType, String actionName, int sort) {
        String name = this.toString();
        DocumentsAction action = new DocumentsAction();
        action.setAction(name);
        action.setActionName(actionName);
        action.setFormType(fromType);
        action.setDocumentsStatusId(status);
        action.setSort(sort);
        actionService.save(action);
    }


}
