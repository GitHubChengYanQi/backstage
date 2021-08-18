package cn.atsoft.dasheng.portal.banner.model.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 返回给前台的通用包装
 */
@Data
@ApiModel(description = "通用返回结果")
public class ResponseData<T> {

    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";

    public static final String DEFAULT_ERROR_MESSAGE = "网络异常";

    public static final Integer DEFAULT_SUCCESS_CODE = 0;

    public static final Integer DEFAULT_ERROR_CODE = 500;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 响应状态码
     */
    @ApiModelProperty("业务状态码")
    private Integer errCode;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应对象
     */
    private T data;

    public ResponseData() {
    }

    public ResponseData(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.errCode = code;
        this.message = message;
        this.data = data;
    }

    public static<T> SuccessResponseData<T> success() {
        return new SuccessResponseData<T>();
    }

    public static<T> SuccessResponseData<T> success(T object) {
        return new SuccessResponseData<T>(object);
    }

    public static<T> SuccessResponseData<T> success(Integer code, String message, T object) {
        return new SuccessResponseData<T>(code, message, object);
    }

    public static ErrorResponseData error(String message) {
        return new ErrorResponseData(message);
    }

    public static ErrorResponseData error(Integer code, String message) {
        return new ErrorResponseData(code, message);
    }

    public static<T> ErrorResponseData error(Integer code, String message, T object) {
        return new ErrorResponseData(code, message, object);
    }
}
