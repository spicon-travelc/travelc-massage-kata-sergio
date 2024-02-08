package com.trc.massage;

import com.trc.massage.binding.Response;
import com.trc.massage.model.MassageService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.Duration.ofMinutes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MassageServiceBOTest {

    private MassageGateway gateway = mock(MassageGateway.class);
    private MassageServiceBO massageServiceBO = new MassageServiceBO(gateway);

    private LocalDate requestDate = LocalDate.of(2025, 2, 8);

    @Test
    public void given_request_date_valid_should_return_six_massages() throws IOException {
        when(gateway.getMassages(any())).thenReturn(getResponseOK());

        var response = massageServiceBO.getMassages(requestDate);

        assertThat(response, hasSize(6));
    }

    @Test
    public void given_request_date_should_return_an_error_when_the_minimum_availability_date_is_15_days() throws IOException {
        when(gateway.getMassages(any())).thenReturn(getResponseKO());

        var exception = assertThrows(IllegalArgumentException.class, () -> massageServiceBO.getMassages(LocalDate.now().plusDays(1)));

        assertThat(exception.getMessage(), is("La antelación mínima para reservar es de 15 días"));
    }

    @Test
    public void given_massage_with_code_one() throws IOException {
        MassageService massageService = getMassageServiceByCode("1");

        assertThat(massageService.code(), is("1"));
        assertThat(massageService.name(), is("Masaje sueco"));
        assertThat(massageService.duration(), is(ofMinutes(30)));
        assertThat(massageService.price().getAmount(), is(25.0));
        assertThat(massageService.price().getCurrency(), is("EUR"));
        assertThat(massageService.policies(), hasSize(2));
        assertThat(massageService.policies().get(0), is("Sin gastos de cancelación hasta el 01 feb 2025."));
        assertThat(massageService.policies().get(1), is("Desde el 02 feb 2025: no reembolsable."));
    }

    @Test
    public void given_massage_with_code_two() throws IOException {
        MassageService massageService = getMassageServiceByCode("2");

        assertThat(massageService.code(), is("2"));
        assertThat(massageService.name(), is("Masaje de Aromaterapia con luces de colores"));
        assertThat(massageService.duration(), is(ofMinutes(85)));
        assertThat(massageService.price().getAmount(), is(300.0));
        assertThat(massageService.price().getCurrency(), is("EUR"));
        assertThat(massageService.policies(), hasSize(1));
        assertThat(massageService.policies().get(0), is("Desde el 08 feb 2025: no reembolsable."));
    }

    @Test
    public void given_massage_with_code_three() throws IOException {
        MassageService massageService = getMassageServiceByCode("3");

        assertThat(massageService.code(), is("3"));
        assertThat(massageService.name(), is("Masaje Deportivo 60 minutos"));
        assertThat(massageService.duration(), is(ofMinutes(60)));
        assertThat(massageService.price().getAmount(), is(60.0));
        assertThat(massageService.price().getCurrency(), is("EUR"));
        assertThat(massageService.policies(), hasSize(3));
        assertThat(massageService.policies().get(0), is("Sin gastos de cancelación hasta el 04 feb 2025."));
        assertThat(massageService.policies().get(1), is("Entre el 05 feb 2025 y el 07 feb 2025: 40.0 EUR."));
        assertThat(massageService.policies().get(2), is("Desde el 08 feb 2025: no reembolsable."));
    }

    @Test
    public void given_massage_with_code_four() throws IOException {
        MassageService massageService = getMassageServiceByCode("4");

        assertThat(massageService.code(), is("4"));
        assertThat(massageService.name(), is("Masaje Deportivo 45 minutos"));
        assertThat(massageService.duration(), is(ofMinutes(45)));
        assertThat(massageService.price().getAmount(), is(45.0));
        assertThat(massageService.price().getCurrency(), is("EUR"));
        assertThat(massageService.policies(), hasSize(2));
        assertThat(massageService.policies().get(0), is("Sin gastos de cancelación hasta el 07 feb 2025."));
        assertThat(massageService.policies().get(1), is("Desde el 08 feb 2025: no reembolsable."));
    }

    @Test
    public void given_massage_with_code_five() throws IOException {
        MassageService massageService = getMassageServiceByCode("5");

        assertThat(massageService.code(), is("5"));
        assertThat(massageService.name(), is("Masaje de piedras calientes"));
        assertThat(massageService.duration(), is(ofMinutes(70)));
        assertThat(massageService.price().getAmount(), is(150.0));
        assertThat(massageService.price().getCurrency(), is("EUR"));
        assertThat(massageService.policies(), hasSize(1));
        assertThat(massageService.policies().get(0), is("Desde el %s: no reembolsable.".formatted(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")))));
    }

    @Test
    public void given_massage_with_code_one_eight() throws IOException {
        MassageService massageService = getMassageServiceByCode("8");

        assertThat(massageService.code(), is("8"));
        assertThat(massageService.name(), is("Masaje de Aromaterapia"));
        assertThat(massageService.duration(), is(ofMinutes(85)));
        assertThat(massageService.price().getAmount(), is(50.0));
        assertThat(massageService.price().getCurrency(), is("EUR"));
        assertThat(massageService.policies(), hasSize(5));
        assertThat(massageService.policies().get(0), is("Sin gastos de cancelación hasta el 23 ene 2025."));
        assertThat(massageService.policies().get(1), is("Entre el 24 ene 2025 y el 02 feb 2025: 15.0 EUR."));
        assertThat(massageService.policies().get(2), is("Entre el 03 feb 2025 y el 05 feb 2025: 25.0 EUR."));
        assertThat(massageService.policies().get(3), is("Entre el 06 feb 2025 y el 07 feb 2025: 40.0 EUR."));
        assertThat(massageService.policies().get(4), is("Desde el 08 feb 2025: no reembolsable."));
    }

    private MassageService getMassageServiceByCode(String code) throws IOException {
        when(gateway.getMassages(any())).thenReturn(getResponseOK());

        var response = massageServiceBO.getMassages(requestDate);

        return response.stream().filter(m -> m.code().equals(code)).findFirst().get();
    }

    private static Response getResponseOK() throws IOException {
        return ObjectMapperFactory.create().readValue(MassageServiceBO.class.getResource("/responseOK.json"), Response.class);
    }

    private static Response getResponseKO() throws IOException {
        return ObjectMapperFactory.create().readValue(MassageServiceBO.class.getResource("/responseKO.json"), Response.class);
    }
}
