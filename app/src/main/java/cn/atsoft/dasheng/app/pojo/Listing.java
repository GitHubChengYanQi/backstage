package cn.atsoft.dasheng.app.pojo;

import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import lombok.Data;

import java.util.List;

@Data
public class Listing {
    private OutstockListingResult listingResult;

    private List<StorehousePositionsResult> positionsResults;
}
