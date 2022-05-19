package com.graduation.parrot.domain.dto;

import lombok.Getter;

@Getter
public class FileDto {

    private String uploadFileName;
    private String storeFileName;

    public FileDto(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
