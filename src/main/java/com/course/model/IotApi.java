package com.course.model;

import lombok.Data;

@Data
public class IotApi {
    private Integer id;
    private String api_address;
    private String request_param;
    private String request_method;
    private String param_type;
    private String assert_value;
    private String case_name;
}
