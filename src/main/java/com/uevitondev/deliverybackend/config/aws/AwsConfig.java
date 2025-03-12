package com.uevitondev.deliverybackend.config.aws;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
class AWSConfig {

    @Value("${aws.access.key.id}")
    private String awsAccessKeyId;
    @Value("${aws.secret.access.key}")
    private String awsSecretAccessKey;

    @Bean
    public S3Client s3Client() {
        var awsCredentials = AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey);
        var credentialsProvider = StaticCredentialsProvider.create(awsCredentials);
        return S3Client.builder()
                .region(Region.SA_EAST_1)
                .credentialsProvider(credentialsProvider)
                .build();

    }


}
