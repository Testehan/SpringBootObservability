package com.testehan.SpringBootObservability.randomEndpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Service;

@Service
@Endpoint(id="randomEndpoint")
public class RandomEndpoint {

    @ReadOperation
    public int random(){
        return (int)(Math.random() * 100);
    }

}
