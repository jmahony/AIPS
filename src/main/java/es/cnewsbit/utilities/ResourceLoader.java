package es.cnewsbit.utilities;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * Loads resources
 */
public class ResourceLoader {

    /**
     *
     * Returns a resource as a string
     *
     * @param obj the object the path is relative from
     * @param resource the resources path
     * @return the resource
     */
    public static String asString(Object obj, String resource) throws IOException {

        return IOUtils.toString(
            obj.getClass().getResourceAsStream(resource),
            "UTF-8"
        );


    }

}
