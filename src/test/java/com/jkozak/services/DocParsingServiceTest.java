package com.jkozak.services;

import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class DocParsingServiceTest {

    private DocParsingService docParsingService;
    private String html;
    private Document doc;

    @Before
    public void setUp() throws Exception {
        docParsingService = new DocParsingService();
        html = "<!DOCTYPE html>\n" +
                "<html lang=\"en-US\">\n" +
                "<head>\n" +
                "<title>HTML Examples</title>" +
                "</head>" +
                "<body>" +
                "<a href='http://www.example.com/example'>example</a>" +
                "<a href='http://www.onet.pl'>example</a>" +
                "<a href='https://onet.pl/moto'>example</a>" +
                "<a href='http://www.example.com'>example</a>" +
                "<a href='http://www.example.com'>example</a>" +
                "<a href='https://www.example.com'>example</a>" +
                "<a href='http://www.poczta.wp.pl'>example</a>" +
                "<a href=''>example</a>" +
                "<a href='/example/example/somelink'>example</a>" +
                "<a href='http://www.poczta.wp.pl/login'>example</a>" +
                "<a href='https://www.example.com'>example</a>" +
                "<a href='http://www.example.com'>example</a>" +
                "</body>" +
                "</html>";
        doc = Jsoup.parse(html);
    }


    @Test
    public void parseDocSuccessfullyTest() throws Exception {
        Map<String, Integer> domainMap = docParsingService.parseDoc(doc);

        Assertions.assertThat(domainMap.size()).isEqualTo(3);
        Assertions.assertThat(domainMap).contains(Assertions.entry("example.com", 6));
        Assertions.assertThat(domainMap).contains(Assertions.entry("onet.pl", 2));
        Assertions.assertThat(domainMap).contains(Assertions.entry("poczta.wp.pl", 2));
    }

    @Test
    public void parseElementsSuccessfullyTest() throws Exception {
        Elements elements = doc.getElementsByTag("a");

        Map<String, Integer> domainMap = docParsingService.parseElements(elements);

        Assertions.assertThat(domainMap.size()).isEqualTo(3);
    }

    @Test
    public void getDomainSuccessfullyTest() throws Exception {
        String href = "http://www.onet.pl/moto";
        String expected = "onet.pl";
        String domain = docParsingService.getDomain(href);

        Assertions.assertThat(domain).isEqualTo(expected);
    }

    @Test
    public void getDomainUnsuccessfullyTest() throws Exception {
        String href = "/moto/jakislink/example";
        String domain = docParsingService.getDomain(href);

        Assertions.assertThat(domain).isNull();
    }
}