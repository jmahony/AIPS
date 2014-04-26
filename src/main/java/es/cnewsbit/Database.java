package es.cnewsbit;

import es.cnewsbit.articles.NewsArticle;
import es.cnewsbit.exceptions.NoDateException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by josh on 06/04/14.
 */
@Log4j2
public class Database {

    /**
     * The connection pool
     */
    private @Getter final BasicDataSource dataSource;

    /**
     * How many items to send per batch to the DB
     */
    private int batchSize;

    /**
     * The size of the current batch, needs to be atomic because multiple
     * thread will be accessing
     */
    private AtomicInteger currentBatchSize = new AtomicInteger(0);

    /**
     * The statement to insert
     */
    private @Getter PreparedStatement s;

    /**
     *
     * @param user DB username
     * @param pass DB password
     * @param name DB name
     * @param size DB pool size
     * @param batchSize how many objects to get a time
     * @throws SQLException if the prepared statement is invalid
     */
    public Database(String user, String pass, String name, int size, int batchSize) throws SQLException {

        this.batchSize = batchSize;

        log.info("Setting up database connection");

        dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername(user);
        dataSource.setPassword(pass);
        dataSource.setUrl(name);
        dataSource.setInitialSize(size);

        Connection c = dataSource.getConnection();

        s = c.prepareStatement("INSERT INTO article VALUES (?, ?, ?, ?, ?, ?)");

    }

    /**
     *
     * Inserts an entry into the batch, and if the batch size is reached,
     * it is sent to the DB
     *
     * @param newsArticle the article to insert
     * @throws SQLException invalid SQL
     */
    public synchronized void insert(NewsArticle newsArticle) throws SQLException {

        try {

            s.setString(1, newsArticle.getHandle());
            s.setString(2, newsArticle.getUrl().toString().trim());
            s.setString(3, newsArticle.getHeadline().trim());
            s.setTimestamp(4, new Timestamp(newsArticle.getDate().getMillis()));
            s.setString(5, newsArticle.getContent().trim());
            s.setString(6, newsArticle.getSummarisation().trim());

            s.addBatch();

            int current = currentBatchSize.incrementAndGet();

            // If we have reached the batch size, send the batch
            if (current >= batchSize) {

                // Reset the batch size
                currentBatchSize.set(0);

                log.info("Sending batch to DB");

                // Send the batch to MySQL
                s.executeBatch();

                // Clear the batch for the next batch
                s.clearBatch();

            }

        } catch (SQLException e) {

            log.info(e.getMessage());

        } catch (NoDateException e) {

            log.debug("Could not extract date");

        }

    }

}
