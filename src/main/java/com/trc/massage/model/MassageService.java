package com.trc.massage.model;

import com.trc.massage.binding.Price;

import java.time.Duration;
import java.util.List;

public record MassageService(String code, String name, Duration duration, Price price, List<String> policies) {


}
