package cn.atsoft.dasheng.portal.navigation.model.result;

import lombok.Data;

import java.util.List;

@Data
public class NavigationRequest {
    private List<Long> navigationId;
}
