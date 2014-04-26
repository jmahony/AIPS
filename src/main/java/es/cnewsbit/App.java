package es.cnewsbit;

import com.googlecode.flyway.core.Flyway;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Application entry point, simply migrates the database and starts the document
 * processor
 * Make another change for push
 */
@Log4j2
public class App {

    public static void main(String[] args) {

        // Keep the MySQL database up to date and migrate the schema
        Flyway flyway = new Flyway();
        flyway.setDataSource(C.DB_NAME, C.DB_USER, C.DB_PASSWORD);
        flyway.migrate();

        try {

            // Start the document processor
            DocumentProcessor dp = new DocumentProcessor();

            // Rebuild truncate the current articles table and start from
            // scratch
            dp.rebuild();

        } catch (IOException e) {

            log.fatal(e.getMessage());

            System.exit(-1);

        } catch (SQLException e) {

            log.fatal(e.getMessage());

            System.exit(-1);

        }

    }

}
