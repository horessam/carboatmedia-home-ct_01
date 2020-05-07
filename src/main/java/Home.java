import com.fasterxml.jackson.databind.ObjectMapper;
import models.Ad;
import services.DetectScamService;
import services.QuotationService;
import services.impl.DetectScamServiceImpl;
import services.impl.MockBlacklistServiceImpl;
import services.impl.MockQuotationServiceImpl;

import java.io.*;

public class Home {

    public static void main(String[] args) {
        try {
            DetectScamServiceImpl detectScamServiceImpl = new DetectScamServiceImpl(new MockQuotationServiceImpl(), new MockBlacklistServiceImpl());
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream input = Home.class.getClassLoader().getResourceAsStream("ad.json");
            Ad ad = objectMapper.readValue(input, Ad.class);
            detectScamServiceImpl.writeScam(ad);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
