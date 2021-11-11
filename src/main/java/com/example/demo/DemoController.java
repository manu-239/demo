package com.example.demo;

import com.sap.xs.env.Credentials;
import com.sap.xs.env.Service;
import com.sap.xs.env.ServiceAttribute;
import com.sap.xs.env.VcapServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/")
    public ResponseEntity<Map> getGreetings(){
        List<Service> services = VcapServices.fromEnvironment().findServices("GoRestDestination", ServiceAttribute.NAME);
        Map<String,String> values = new HashMap<>();
        Credentials credentials = services.get(0).getCredentials();
        values.put("URL", credentials.get("url") + "/oauth/token");
        values.put("ClientId", String.valueOf(credentials.get("clientid")));
        values.put("ClientSecret", String.valueOf(credentials.get("clientsecret")));
        values.put("password", services.get(0).getCredentials().getPassword());
        return new ResponseEntity<>(values, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/gorest")
    public String getGoRest(){
        return restTemplate.exchange("https://gorest.co.in/public/v1/posts", HttpMethod.GET, new HttpEntity<>(null), String.class).getBody();
    }
}
