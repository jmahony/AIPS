package es.cnewsbit.indexers;

import es.cnewsbit.Indexable;

import java.io.IOException;

/**
 * Interface for indexers, so custom indexers can be used easily
 */
public interface Indexer {

    void addToIndex(Indexable indexable) throws IOException;

    void close() throws IOException;

}
