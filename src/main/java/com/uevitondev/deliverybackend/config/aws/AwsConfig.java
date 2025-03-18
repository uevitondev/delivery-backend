package com.uevitondev.deliverybackend.config.aws;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
class AWSConfig {

    @Value("${aws.access.key.id}")
    private String awsAccessKeyId;
    @Value("${aws.secret.access.key}")
    private String awsSecretAccessKey;
    private static final String awsRegion = "sa-east-1";

    @Bean
    public S3Client getS3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey);
        return S3Client
                .builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }


}
