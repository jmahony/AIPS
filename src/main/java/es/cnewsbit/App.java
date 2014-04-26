package es.cnewsbit;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.googlecode.flyway.core.Flyway;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Application entry point, simply migrates the database and starts the document
 * processor
 */
@Log4j2
public class App {

    public static void main(String[] args) {

        // Load configuration
        try {

            String configString = Files.toString(new File("config.json"), Charsets.UTF_8);

            JSONParser parser = new JSONParser();

            JSONObject config = (JSONObject) parser.parse(configString);

            // Load database configuration
            JSONObject databaseConfig = (JSONObject) config.get("database");

            C.DB_NAME     = databaseConfig.get("db_name").toString();
            C.DB_USER     = databaseConfig.get("db_user").toString();
            C.DB_PASSWORD = databaseConfig.get("db_pass").toString();

        } catch (IOException | ParseException e) {

            log.fatal(e.getMessage());

            System.exit(-1);

        }

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
