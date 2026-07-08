package com.ai.sync_law.law.feign;


import com.ai.sync_law.law.feign.response.LawDetailResponse;
import com.ai.sync_law.law.feign.response.LawSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "law-api-client", url = "${infra.national-law.url}")
public interface LawApiClient {

    @GetMapping("/lawSearch.do")
    LawSearchResponse getLawArticles (
            @RequestParam("OC") String openId,
            @RequestParam(value = "target", defaultValue = "law") String target,
            @RequestParam("type") String type,
            @RequestParam("query") String query,
            @RequestParam("display") int display,
            @RequestParam("start") int page
    );

    @GetMapping("/lawService.do")
    LawDetailResponse getLawDetail(
            @RequestParam("OC") String openId,
            @RequestParam(value = "target", defaultValue = "law") String target,
            @RequestParam("type") String type,
            @RequestParam("MST") String masterId
    );
}
