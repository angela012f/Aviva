package com.hmhs.aem.hgl.core.services;
import java.util.Map;

public interface CurrencyConversionService {
    Map<String, Map<String, String>> convertPrice(double price, String[] currencies);

}
