package uk.gov.hmcts.evidence.management.service;

import org.springframework.web.multipart.MultipartFile;

import uk.gov.hmcts.evidence.management.domain.StoredFileHalResource;

public interface StorageService {
    StoredFileHalResource store(MultipartFile file);
}
