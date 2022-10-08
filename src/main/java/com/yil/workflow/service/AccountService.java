/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.service;

import com.yil.workflow.base.ApiConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {
    public boolean existsPermission(long permissionId, long userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders h2 = new HttpHeaders();
        h2.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        h2.add(ApiConstant.AUTHENTICATED_USER_ID, String.valueOf(1));
        String uri = "http://localhost:8082/api/account/v1/users/{id}/permission-id={permissionId}";
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(h2), String.class, Map.of("id", userId, "permissionId", permissionId));
        return responseEntity.getStatusCode().equals(HttpStatus.OK);
    }

    public boolean existsPermission(long permissionId) {
        return true;
    }
}
