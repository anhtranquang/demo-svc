package com.finx.contractservice.infra.client.fptesign;

import com.finx.contractservice.infra.client.fptesign.dto.SignCloudReq;
import com.finx.contractservice.infra.client.fptesign.dto.SignCloudResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "fpt-esign-feign", url = "${client.fptEsign.apiconfig.url}")
public interface FptESignFeignClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/prepareCertificateForSignCloud",
            headers = {"Content-Type: application/json"})
    SignCloudResp prepareCertificateForSignCloud(SignCloudReq request);

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/prepareFileForSignCloud",
            headers = {"Content-Type: application/json"})
    SignCloudResp prepareFileForSignCloud(SignCloudReq request);

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/regenerateAuthorizationCodeForSignCloud",
            headers = {"Content-Type: application/json"})
    SignCloudResp regenerateAuthorizationCodeForSignCloud(SignCloudReq request);

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/getCertificateDetailForSignCloud",
            headers = {"Content-Type: application/json"})
    SignCloudResp getCertificateDetailForSignCloud(SignCloudReq request);

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/authorizeCounterSigningForSignCloud",
            headers = {"Content-Type: application/json"})
    SignCloudResp authorizeCounterSigningForSignCloud(SignCloudReq request);
}
