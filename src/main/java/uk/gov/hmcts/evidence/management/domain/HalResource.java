package uk.gov.hmcts.evidence.management.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class HalResource extends ResourceSupport {

    private final Map<String, ResourceSupport> embedded = new HashMap<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("_embedded")
    public Map<String, ResourceSupport> getEmbeddedResources() {
        return embedded;
    }

    public void embedResource(String relationship, ResourceSupport resource) {
        embedded.put(relationship, resource);
    }
}