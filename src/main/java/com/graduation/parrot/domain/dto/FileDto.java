package com.graduation.parrot.domain.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FileDto {

    private String uploadFileName;
    private String storeFileName;
    private String pathUrl;

    public FileDto(String uploadFileName, String storeFileName, String pathUrl) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.pathUrl = pathUrl;
    }
}
