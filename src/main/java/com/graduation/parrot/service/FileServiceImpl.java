package com.graduation.parrot.service;

import com.graduation.parrot.domain.File;
import com.graduation.parrot.domain.dto.FileDto;
import com.graduation.parrot.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    public static final String webFilePath = "/file/";
    private final FileRepository fileRepository;
    @Value("${file.dir}")
    private String fileDir;

    @Override
    public FileDto saveFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String pathUrl = webFilePath + storeFileName;
        //saveFile
        multipartFile.transferTo(new java.io.File(getFullPath(storeFileName)));
        //save in DB
        fileRepository.save(new File(originalFilename, storeFileName));

        return new FileDto(originalFilename, storeFileName, pathUrl);
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
