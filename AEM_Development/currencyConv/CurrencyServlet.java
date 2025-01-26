package com.hmhs.aem.hgl.core.servlets;

import com.google.gson.Gson;
import com.hmhs.aem.hgl.core.services.CurrencyConversionService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.text.NumberFormat;


@Component(service = Servlet.class,
        property = {
                org.osgi.framework.Constants.SERVICE_DESCRIPTION + "=Currency Servlet",
                "sling.servlet.methods=GET",
                "sling.servlet.paths=" + "/bin/currencyConversion",
                "sling.servlet.extensions=" + "json",
                "sling.servlet.metatype=" + "true",
        })
public class CurrencyServlet extends SlingSafeMethodsServlet {
    @Reference
    private CurrencyConversionService currencyConversionService;

    private static final Gson GSON = new Gson();

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String priceParam = request.getParameter("price");
        String currenciesParam = request.getParameter("currencies");

        if (priceParam == null || currenciesParam == null) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing required parameters: 'price' and 'currencies'.");
            return;
        }

        try {
            double price = Double.parseDouble(priceParam);
            String[] currencies = currenciesParam.split(",");

            Map<String, Map<String, String>> result = currencyConversionService.convertPrice(price, currencies);

            response.setContentType("application/json");
            response.getWriter().write(GSON.toJson(result));
        } catch (NumberFormatException e) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid price format. Must be a number.");
        }
    }
}