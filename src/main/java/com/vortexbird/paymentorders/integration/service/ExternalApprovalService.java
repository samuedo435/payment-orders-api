package com.vortexbird.paymentorders.integration.service;

import com.vortexbird.paymentorders.integration.dto.ApprovalNotificationRequest;
import com.vortexbird.paymentorders.integration.dto.ApprovalNotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Gestiona la comunicación con
 * sistemas externos.
 */
@Service
@Slf4j
public class ExternalApprovalService {

    private final RestClient restClient;

    public ExternalApprovalService() {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    /**
     * Notifica la aprobación de una orden
     * a un sistema externo.
     */
    public void notifyOrderApproved(
            ApprovalNotificationRequest request
    ) {

        try {

            ApprovalNotificationResponse response =
                    restClient.post()
                            .uri("/posts")
                            .body(request)
                            .retrieve()
                            .body(
                                    ApprovalNotificationResponse.class
                            );

            log.info(
                    "External notification sent successfully. Response id={}",
                    response != null ? response.id() : null
            );

        } catch (Exception ex) {

            log.error(
                    "Error notifying external system: {}",
                    ex.getMessage()
            );
        }
    }
}