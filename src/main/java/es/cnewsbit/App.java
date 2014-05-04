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

        final String DB_NAME, DB_USER, DB_PASS;

        // Load configuration
        try {

            String configString = Files.toString(new File("config.json"), Charsets.UTF_8);

            JSONParser parser = new JSONParser();

            JSONObject config = (JSONObject) parser.parse(configString);

            // Load database configuration
            JSONObject databaseConfig = (JSONObject) config.get("database");

            DB_NAME = databaseConfig.get("db_name").toString();
            DB_USER = databaseConfig.get("db_user").toString();
            DB_PASS = databaseConfig.get("db_pass").toString();

            // Keep the MySQL database up to date and migrate the schema
            Flyway flyway = new Flyway();
            flyway.setDataSource(DB_NAME, DB_USER, DB_PASS);
            flyway.migrate();

            Database database = new Database(DB_USER,
                                             DB_PASS,
                                             DB_NAME,
                                             C.DB_POOL_SIZE,
                                             C.DB_BATCH_SIZE);

            // Start the document processor
            DocumentProcessor dp = new DocumentProcessor(database);

            // Rebuild truncate the current articles table and start from
            // scratch
            dp.rebuild(HTMLStore.getInstance());

        } catch (IOException | SQLException | ParseException e) {

            log.fatal(e.getMessage());

            System.exit(-1);

        }

    }

}
