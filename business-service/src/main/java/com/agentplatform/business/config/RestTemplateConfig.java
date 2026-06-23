package com.agentplatform.business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${agent-service.url}")
    private String agentServiceUrl;

    @Value("${rag-service.url}")
    private String ragServiceUrl;

    @Value("${gpu-node.url}")
    private String gpuNodeUrl;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(60000); // Long timeout for AI calls
        return new RestTemplate(factory);
    }

    public String getAgentServiceUrl() {
        return agentServiceUrl;
    }

    public String getRagServiceUrl() {
        return ragServiceUrl;
    }

    public String getGpuNodeUrl() {
        return gpuNodeUrl;
    }
}
