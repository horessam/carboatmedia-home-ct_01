package services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Ad;
import models.Contact;
import models.Phone1;
import models.Scam;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class DetectScamServiceImplTest {
    private DetectScamServiceImpl detectScamService = new DetectScamServiceImpl(new MockQuotationServiceImpl(), new MockBlacklistServiceImpl());
    private Ad ad;
    private Ad scam;
    private Contact contactCorrect = new Contact ("bob", "leponge", "bobleponge@gmail.com", new Phone1("0123456789"));
    private Contact contactIncorrect = new Contact ("b", "l", "b-----b@gmail.com", new Phone1("0123456789"));
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream input = getClass().getClassLoader().getResourceAsStream("adCorrect.json");
            InputStream inputScam = getClass().getClassLoader().getResourceAsStream("ad.json");
            ad = objectMapper.readValue(input, Ad.class);
            scam = objectMapper.readValue(inputScam, Ad.class);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void AdNotScam() {
        Scam expected = new Scam ("B300053623", false, new String[] {});
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            detectScamService.writeScam(ad);
            Assert.assertEquals(objectMapper.writeValueAsString(expected), outContent.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ScamWithFirstNameIncorrect() {
        ad.getContacts().setFirstName("b");
        Scam expected = new Scam ("B300053623", true, new String[] {"rule::firstname::length"});
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            detectScamService.writeScam(ad);
            Assert.assertEquals(objectMapper.writeValueAsString(expected), outContent.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ScamWithLastNameIncorrect() {
        ad.getContacts().setLastName("b");
        Scam expected = new Scam ("B300053623", true, new String[] {"rule::lastname::length"});
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            detectScamService.writeScam(ad);
            Assert.assertEquals(objectMapper.writeValueAsString(expected), outContent.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ScamWithEmailAlphaRateIncorrect() {
        ad.getContacts().setEmail("b-o-b-l-e-p-o-n-g-e@gmail.com");
        Scam expected = new Scam ("B300053623", true, new String[] {"rule:✉:alpha_rate"});
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            detectScamService.writeScam(ad);
            Assert.assertEquals(objectMapper.writeValueAsString(expected), outContent.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ScamWithEmailNumberRateIncorrect() {
        ad.getContacts().setEmail("bob77878@gmail.com");
        Scam expected = new Scam ("B300053623", true, new String[] {"rule:✉:number_rate"});
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            detectScamService.writeScam(ad);
            Assert.assertEquals(objectMapper.writeValueAsString(expected), outContent.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ScamWithQuotationRateIncorrect() {
        ad.setPrice(10000);
        Scam expected = new Scam ("B300053623", true, new String[] {"rule::price::quotation_rate"});
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            detectScamService.writeScam(ad);
            Assert.assertEquals(objectMapper.writeValueAsString(expected), outContent.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ScamWithRegisterNumberBlackListed() {
        ad.getVehicle().setRegisterNumber("AA123AA");
        Scam expected = new Scam ("B300053623", true, new String[] {"rule::registernumber::blacklist"});
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            detectScamService.writeScam(ad);
            Assert.assertEquals(objectMapper.writeValueAsString(expected), outContent.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void AdScam() {
        Scam expected = new Scam ("B300053623", true, new String[] {"rule::price::quotation_rate","rule::registernumber::blacklist"});
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            detectScamService.writeScam(scam);
            Assert.assertEquals(objectMapper.writeValueAsString(expected), outContent.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}