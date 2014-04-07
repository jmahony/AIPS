package es.cnewsbit.indexers;

import es.cnewsbit.Indexable;

import java.io.IOException;

/**
 * Created by josh on 03/04/14.
 */
public interface Indexer {

    void addToIndex(Indexable indexable)  throws IOException;

    void close() throws IOException;

}
