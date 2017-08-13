package com.jkozak;

import com.jkozak.services.DocParsingService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String URL = "http://wawalove.pl/";

    public static void main(String[] args) {
        DocParsingService docParsingService = new DocParsingService();

        Document doc;
        try {
            doc = Jsoup.connect(URL).get();
            Map<String, Integer> urisMap = docParsingService.parseDoc(doc);

            urisMap.forEach((key, value) -> System.out.println("- " + key + " - " + value));

        } catch (IOException e) {
            logger.error("Error while connecting: " + e.getMessage());
        }
    }
}
