package es.cnewsbit.indexers;

import es.cnewsbit.Indexable;

import java.io.IOException;

public interface Indexer {

    void addToIndex(Indexable indexable) throws IOException;

    void close() throws IOException;

}
