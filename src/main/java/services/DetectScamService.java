package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import models.Ad;
import models.Scam;

public interface DetectScamService {
    Scam detectScam (Ad ad) throws JsonProcessingException;
    void writeScam (Ad ad);
}
