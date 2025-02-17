package com.ztf;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configurable
//一定要加上这个注解，否则job无法运行
@EnableScheduling
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

}
