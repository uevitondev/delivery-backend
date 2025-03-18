package com.uevitondev.deliverybackend.config.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3ClientConfig {

    @Bean
    public S3Client amazonS3() {
        return S3Client.builder()
                .region(Region.SA_EAST_1)
                .build();
    }
}
