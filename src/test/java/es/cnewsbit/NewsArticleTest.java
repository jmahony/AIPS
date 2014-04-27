package es.cnewsbit;

import es.cnewsbit.articles.BBCNewsArticle;
import es.cnewsbit.articles.NewsArticle;
import es.cnewsbit.exceptions.NotNewsArticleException;
import es.cnewsbit.utilities.ResourceLoader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by joshmahony on 27/04/2014.
 */
public class NewsArticleTest {

    private Document dom;

    @Before
    public void setup() throws IOException {

        String html = ResourceLoader.asString(this, "/simple.html");

        dom = Jsoup.parse(html);

    }

    @Test
    public void testGetMetaKeywords() throws NotNewsArticleException, MalformedURLException {

        HTMLDocument document = mock(HTMLDocument.class);

        List keywords = new ArrayList<String>() {{
            add("one");
            add("two");
            add("three");
            add("four");
        }};

        when(document.getMetaKeywords()).thenReturn(keywords);

        when(document.getDom()).thenReturn(dom);

        NewsArticle newsArticle = new BBCNewsArticle(document, new URL("http://www.bbc.co.uk"));

        assertEquals(newsArticle.getMetaKeywords(), keywords);

    }

}
