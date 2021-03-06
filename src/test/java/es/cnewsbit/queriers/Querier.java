package es.cnewsbit.queriers;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

/**
 * Created by josh on 06/04/14.
 */
public interface Querier {

    String[] query(String query, int limit) throws ParseException, IOException;
    void close() throws IOException;

}
