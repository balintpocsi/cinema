package com.thesis.recommenderapp.domain;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UploadItemRequest {

    @NotEmpty(message = "Title or URL cannot be empty")
    private String titleOrURL;
    @NotEmpty(message = "Type cannot be empty")
    private String type;

}
