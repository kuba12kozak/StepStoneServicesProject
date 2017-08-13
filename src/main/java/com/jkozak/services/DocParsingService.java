package com.jkozak.services;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DocParsingService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Map<String, Integer> parseDoc(Document doc) {
        Elements anhorElements = doc.getElementsByTag("a");
        Map<String, Integer> domainMap = parseElements(anhorElements);

        domainMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return domainMap;
    }

    Map<String, Integer> parseElements(Elements anhorElements) {
        Map<String, Integer> domainMap = new LinkedHashMap<>();

        for (Element element : anhorElements) {
            String href = element.attr("href");

            if (StringUtils.isBlank(href)) {
                continue;
            }

            String domain = getDomain(href);

            if (domain == null) {
                continue;
            }

            if (domainMap.containsKey(domain)) {
                domainMap.put(domain, domainMap.get(domain) + 1);
            } else {
                domainMap.put(domain, 1);
            }
        }

        return domainMap;
    }

    String getDomain(String href) {
        URI uri;
        String domain = null;

        try {
            uri = new URI(href);
            domain = uri.getHost();
            domain = (StringUtils.isNotBlank(domain) && domain.startsWith("www.")) ? domain.substring(4) : domain;
        } catch (URISyntaxException e) {
            logger.error("Error while getting domain from url: " + e.getMessage());
        }
        return domain;
    }


}
