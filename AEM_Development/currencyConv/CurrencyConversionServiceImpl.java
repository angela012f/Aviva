package com.hmhs.aem.hgl.core.services.impl;


import com.hmhs.aem.hgl.core.services.CurrencyConversionService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;
import java.util.Map;

@Component(service = CurrencyConversionService.class, immediate = true)
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private static final String CURRENCY_ROOT_PATH = "/etc/currencies/";

    @Reference
    private ResourceResolver resourceResolver;

    @Override
    public Map<String, Map<String, String>> convertPrice(double price, String[] currencies) {
        Map<String, Map<String, String>> currencyData = new HashMap<>();

        for (String currencyCode : currencies) {
            Resource currencyResource = resourceResolver.getResource(CURRENCY_ROOT_PATH + currencyCode);
            if (currencyResource != null) {
                String name = currencyResource.getValueMap().get("name", String.class);
                Double conversionFactor = currencyResource.getValueMap().get("conversionFactor", Double.class);

                if (name != null && conversionFactor != null) {
                    double convertedPrice = price * conversionFactor;
                    Map<String, String> details = new HashMap<>();
                    details.put("name", name);
                    details.put("price", String.format("%.2f", convertedPrice));
                    currencyData.put(currencyCode, details);
                }
            }
        }
        return currencyData;
    }
}
