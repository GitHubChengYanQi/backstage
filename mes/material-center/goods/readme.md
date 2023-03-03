# 基础物料管理功能说明

## 包结构及命名规则

### **brand 品牌管理**

* RestBrandController
```
  @RequestMapping("/brand/{version}")
  @ApiVersion("2.0")
  ```
* RestBrandService
* RestBrandParam
* RestBrandResult
* RestBrandMapper

### **category 分类管理**

* RestCategoryController
* RestCategoryService
* RestCategoryParam
* RestCategoryResult
* RestCategoryMapper

### **class 类目及属性**

* RestClassController
* RestClassService
* RestClassParam
* RestClassResult
* RestClassMapper

### **sku**

* RestSkuController
* RestSkuService
* RestSkuParam
* RestSkuResult
* RestSkuMapper

### **spu**

* RestSpuController
```
  @RequestMapping("/spu/{version}")
  @ApiVersion("2.0")
  ```
* RestSpuService
* RestSpuParam
* RestSpuResult
* RestSpuMapper

### **textrue 材质管理**

* RestUnitController

* RestUnitService
* RestUnitParam
* RestUnitResult
* RestUnitMapper

### **unit 物料单位管理**

* RestUnitController
* RestUnitService
* RestUnitParam
* RestUnitResult
* RestUnitMapper

