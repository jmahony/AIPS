package es.cnewsbit;

import es.cnewsbit.HTMLDocument;
import es.cnewsbit.NewsArticle;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Element;

import java.net.URL;

/**
 * Created by josh on 15/04/14.
 */
@Log4j2
public class UKReutersNewsArticle extends NewsArticle {

    private static final String dateFormat = "yyyy/MM/dd HH:mm:SS";

    /**
     * Constructor
     *
     * @param document the document of the news article
     * @param url
     */
    public UKReutersNewsArticle(HTMLDocument document, URL url) {

        super(document, url);

    }

    public DateTime getDate() {

        Element elem = document.getDom().select("META[name=parsely-page]").first();

        if (elem != null) {

            //DateTimeFormatter dtf = DateTimeFormat.forPattern(dateFormat);

            JSONParser parser = new JSONParser();

            try {

                JSONObject json = (JSONObject) parser.parse(elem.attr("content").toString());

                String dateString = json.get("pub_date").toString();

                DateTime dt = DateTime.parse(dateString);

                return dt;

            } catch (ParseException e) {

                log.error("Could not parse date from article");

            }

        }

        return new DateTime();

    }
}
