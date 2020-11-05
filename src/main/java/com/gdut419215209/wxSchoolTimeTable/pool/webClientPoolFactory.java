package com.example.demo.pool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class webClientPoolFactory {

    private static webClientPool webClientPool;

    public webClientPoolFactory(@Autowired webClientPool webClientPool){
        this.webClientPool=webClientPool;
    }

    public static webClientPool getWebClientPool(){
        return webClientPool;
    }

}
