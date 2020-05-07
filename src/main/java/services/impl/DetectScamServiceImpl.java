package services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Ad;
import models.Contact;
import models.Scam;
import services.BlacklistService;
import services.DetectScamService;
import services.QuotationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class DetectScamServiceImpl implements DetectScamService {
    private static final String RULE_FIRSTNAME_LENGTH_KEY = "rule::firstname::length";
    private static final String RULE_LASTNAME_LENGTH_KEY = "rule::lastname::length";
    private static final String RULE_EMAIL_ALPHA_RATE_KEY = "rule:✉:alpha_rate";
    private static final String RULE_EMAIL_NUMBER_RATE_KEY = "rule:✉:number_rate";
    private static final String RULE_QUOTATION_RATE_KEY = "rule::price::quotation_rate";
    private static final String RULE_REGISTER_NUMBER_KEY = "rule::registernumber::blacklist";
    private QuotationService quotationService;
    private BlacklistService blacklistService;
    private Predicate<Contact> RULE_FIRST_NAME = (Contact contact) -> contact.getFirstName().length() > 2;
    private Predicate<Contact> RULE_LAST_NAME = (Contact contact) -> contact.getLastName().length() > 2;

    private Predicate<Contact> RULE_EMAIL_ALPHA_RATE = (Contact contact) -> {
        char[] firstPartEmail = contact.getEmail().split("@")[0].toCharArray();
        int numberOfAlpha = 0;
        for (char c : firstPartEmail) {
            if (Character.isLetterOrDigit(c)) {
                numberOfAlpha++;
            }
        }
        return (((double) numberOfAlpha / (double) firstPartEmail.length) > 0.7);
    };

    private Predicate<Contact> RULE_EMAIL_NUMBER_RATE = (Contact contact) -> {
        char[] firstPartEmail = contact.getEmail().split("@")[0].toCharArray();
        int numberOfAlpha = 0;
        for (char c : firstPartEmail) {
            if (Character.isDigit(c)) {
                numberOfAlpha++;
            }
        }
        return ((double) numberOfAlpha / (double) firstPartEmail.length < 0.3);
    };

    private Predicate<Ad> RULE_QUOTATION_RATE = (Ad ad) -> {
        int price = ad.getPrice();
        int quotation = 0;
        try {
            quotation = this.quotationService.calculateQuotation(ad.getVehicle()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return (price >= quotation - (quotation * 0.2) && price <= quotation + (quotation * 0.2));
    };

    private Predicate<String> RULE_REGISTER_NUMBER = (String s) -> {
        boolean result = false;
        try {
            result = blacklistService.blacklistRegisterNumber(s).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    };

    public DetectScamServiceImpl(QuotationService quotationService, BlacklistService blacklistService) {
        this.quotationService = quotationService;
        this.blacklistService = blacklistService;
    }

    @Override
    public Scam detectScam(Ad ad) {
        List<String> detectedRules = new ArrayList<String>();
        Contact contact = ad.getContacts();

        if (!RULE_FIRST_NAME.test(contact)) {
            detectedRules.add(RULE_FIRSTNAME_LENGTH_KEY);
        }

        if (!RULE_LAST_NAME.test(contact)) {
            detectedRules.add(RULE_LASTNAME_LENGTH_KEY);
        }

        if (!RULE_EMAIL_ALPHA_RATE.test(contact)) {
            detectedRules.add(RULE_EMAIL_ALPHA_RATE_KEY);
        }

        if (!RULE_EMAIL_NUMBER_RATE.test(contact)) {
            detectedRules.add(RULE_EMAIL_NUMBER_RATE_KEY);
        }

        if (!RULE_QUOTATION_RATE.test(ad)) {
            detectedRules.add(RULE_QUOTATION_RATE_KEY);
        }

        if (RULE_REGISTER_NUMBER.test(ad.getVehicle().getRegisterNumber())) {
            detectedRules.add(RULE_REGISTER_NUMBER_KEY);
        }

        return new Scam(ad.getReference(), detectedRules.size() >= 1, detectedRules.toArray(new String[0]));
    }

    @Override
    public void writeScam(Ad ad) {
        Scam scam = detectScam(ad);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.print(objectMapper.writeValueAsString(scam));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
