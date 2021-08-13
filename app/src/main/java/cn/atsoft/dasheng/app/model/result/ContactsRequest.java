package cn.atsoft.dasheng.app.model.result;

import lombok.Data;

import java.util.List;

@Data
public class ContactsRequest {

    private List<Long> contactsId;
}
