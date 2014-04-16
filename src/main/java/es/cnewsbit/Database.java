package es.cnewsbit;

import es.cnewsbit.articles.NewsArticle;
import es.cnewsbit.exceptions.NoDateExeception;
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

    private @Getter final BasicDataSource dataSource;

    private int batchSizeLimit = 50;
    private AtomicInteger currentBatchSize = new AtomicInteger(0);

    private @Getter PreparedStatement s;

    public Database(String user, String pass, String name, int size) throws SQLException {

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

    public synchronized void insert(NewsArticle newsArticle) throws SQLException {

        try {

            s.setString(1, newsArticle.getHandle());
            s.setString(2, newsArticle.getUrl().toString().trim());
            s.setString(3, newsArticle.getHeading().trim());
            s.setTimestamp(4, new Timestamp(newsArticle.getDate().getMillis()));
            s.setString(5, newsArticle.getContent().trim());
            s.setString(6, newsArticle.getSummarisation().trim());

            s.addBatch();

            int current = currentBatchSize.incrementAndGet();

            if (current >= batchSizeLimit) {
                currentBatchSize.set(0);
                log.info("Sending batch to DB");
                s.executeBatch();
                s.clearBatch();

            }

        } catch (SQLException e) {

            log.info(e.getMessage());

        } catch (NoDateExeception e) {

            log.debug("Could not extract date");

        }

    }

}
