package com.trc.massage;

import com.trc.massage.model.MassageService;

import java.time.LocalDate;
import java.util.List;

import static com.trc.massage.mapper.MassageResponseMapper.buildMassages;

public class MassageServiceBO {

    private final MassageGateway gateway;

    public MassageServiceBO(MassageGateway gateway) {
        this.gateway = gateway;
    }

    public List<MassageService> getMassages(LocalDate requestDate) {
        var response = gateway.getMassages(requestDate);
        if (response.getError() != null) {
            throw new IllegalArgumentException(response.getError());
        }
        return buildMassages(response, requestDate);
    }
}
