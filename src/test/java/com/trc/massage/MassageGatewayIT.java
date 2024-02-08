package com.trc.massage;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class MassageGatewayIT {

    private MassageGateway gateway = new MassageGateway();

    @Test
    public void given_request_date_valid_should_return_massages() {
        var response = gateway.getMassages(LocalDate.now().plusDays(25));

        assertThat(response.getMassages().size(), greaterThan(0));
        assertThat(response.getError(), nullValue());
    }
}
