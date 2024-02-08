package com.trc.massage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.trc.massage.binding.Response;
import org.apache.cxf.jaxrs.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MassageGateway {

    private static final String ENDPOINT = "http://localhost:38080/massages/";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperFactory.create();

    public Response getMassages(LocalDate date) {
        var webClient = WebClient.create(ENDPOINT + "quote/" + date.format(DATE_TIME_FORMATTER), List.of(new JacksonJsonProvider(OBJECT_MAPPER)));
        return webClient.get(Response.class);
    }
}
