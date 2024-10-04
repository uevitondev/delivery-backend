package com.uevitondev.deliverybackend.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uevitondev.deliverybackend.domain.address.AddressViaCepDTO;
import com.uevitondev.deliverybackend.domain.exception.ViaCepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ViaCepService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViaCepService.class);

    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/";

    private final ObjectMapper objectMapper;


    ViaCepService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public AddressViaCepDTO findAddressByCep(String cep) {
        try (var httpClient = HttpClient.newHttpClient()) {
            var finalViaCepUrlRequest = VIA_CEP_URL + cep + "/json";
            var request = HttpRequest.newBuilder()
                    .uri(new URI(finalViaCepUrlRequest))
                    .GET()
                    .build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), AddressViaCepDTO.class);
        } catch (URISyntaxException | InterruptedException | IOException e) {
            LOGGER.error("via cep api error!");
            Thread.currentThread().interrupt();
            throw new ViaCepException("via cep api error");
        }

    }

}
