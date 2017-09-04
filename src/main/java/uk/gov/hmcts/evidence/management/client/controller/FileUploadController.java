package uk.gov.hmcts.evidence.management.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import uk.gov.hmcts.evidence.management.domain.StoredFileHalResource;
import uk.gov.hmcts.evidence.management.exception.StorageFileNotFoundException;
import uk.gov.hmcts.evidence.management.service.StorageService;

@RestController
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value="/api/version/1/uploadFile",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public StoredFileHalResource handleFileUpload(@RequestParam("file") MultipartFile file) {
        return storageService.store(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
