package com.trc.massage.mapper;

import com.trc.massage.binding.Price;

import java.time.LocalDate;

public record CancellationPolicyRange(LocalDate startDate, LocalDate endDate, Price price) {


}
