package ru.netology;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.stream.Stream;

/*xpZQYldklACAA8eW6e7RzFeHqPlGJUNyqezI26SW*/
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static final ObjectMapper mapper = new ObjectMapper();


    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();){
            HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=xpZQYldklACAA8eW6e7RzFeHqPlGJUNyqezI26SW");
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
            try (CloseableHttpResponse response = httpClient.execute(request);) {

                Nasa nasaList = mapper.readValue(response.getEntity().getContent(), new TypeReference<Nasa>() {
                });

                List<String> url = List.of(nasaList.getUrl().split("/"));
                String filename = url.getLast();

                HttpGet request2 = new HttpGet(nasaList.getUrl());
                CloseableHttpResponse response2 = httpClient.execute(request2);
                try (FileOutputStream out = new FileOutputStream(filename)) {
                    byte[] bodyBytes = response2.getEntity().getContent().readAllBytes();
                    out.write(bodyBytes);
                }

                //byte[] bodyBytes = response.getEntity().getContent().readAllBytes();
                /*String body = new String(bodyBytes, StandardCharsets.UTF_8);
                System.out.println(body);*/
            }

        }
    }
}