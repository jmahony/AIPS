package es.cnewsbit;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by josh on 06/04/14.
 */
@Log4j2
public class Database {

    private @Getter final BasicDataSource ds;

    public Database() {

        log.info("Setting up database connection");

        ds = new BasicDataSource();

        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(C.DB_USER);
        ds.setPassword(C.DB_PASSWORD);
        ds.setUrl(C.DB_NAME);
        ds.setInitialSize(4);

        try {

            Connection c = ds.getConnection();

            Statement s = c.createStatement();

            s.execute("TRUNCATE article");

            c.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public void insert(NewsArticle newsArticle) throws SQLException {

        Connection c = ds.getConnection();

        try {

            PreparedStatement s = null;

            s = c.prepareStatement("INSERT INTO article VALUES (?, ?, ?, ?, ?, ?)");

            s.setString(1, newsArticle.getHandle());
            s.setString(2, newsArticle.getUrl().toString());
            s.setString(3, newsArticle.getHeading());
            s.setLong(4, newsArticle.getDate());
            s.setString(5, newsArticle.getContent());
            s.setString(6, newsArticle.getSummarisation());

            log.debug("Storing " + newsArticle.getHandle());

            s.execute();

        } catch (SQLException e) {

            log.info(e.getMessage());

        } finally {

            c.close();

        }

    }

}
