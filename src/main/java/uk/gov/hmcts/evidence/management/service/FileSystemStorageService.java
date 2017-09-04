package uk.gov.hmcts.evidence.management.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import uk.gov.hmcts.evidence.management.domain.StoredFileHalResource;

@Service
public class FileSystemStorageService implements StorageService {

	@Value("${user.authorization}")
	private String authorizationHeader;
	
	@Value("${service.authorization}")
	private String serviceAuthorizationHeader;
	
	@Value("${evidence.management.upload.file.url}")
	private String evidenceManagementServiceURL;
	
	@Override
	public StoredFileHalResource store(MultipartFile file) {
		
		HttpHeaders headers=new HttpHeaders();
		headers.add("Authorization", authorizationHeader);
		headers.add("ServiceAuthorization",serviceAuthorizationHeader);
		
		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("file", file);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.registerModule(new Jackson2HalModule());

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(mapper);

		RestTemplate template = new RestTemplate(Collections.<HttpMessageConverter<?>> singletonList(converter));
		
		ResponseEntity<Resource<StoredFileHalResource>> responseEntity =
				template.exchange(evidenceManagementServiceURL, 
						HttpMethod.POST, 
						new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), 
						new ParameterizedTypeReference<Resource<StoredFileHalResource>>() {}, 
						Collections.emptyMap());
		
		StoredFileHalResource fileHalResource=responseEntity.getBody().getContent();
		
		//fileHalResource.setFileURL(responseEntity.getBody().getLink("binary").getHref());
		
		return fileHalResource;
	}
}
