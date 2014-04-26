package es.cnewsbit.utilities;

import org.apache.commons.io.IOUtils;

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
    public static String asString(Object obj, String resource) {

        try {

            return IOUtils.toString(
                    obj.getClass().getResourceAsStream(resource),
                "UTF-8"
            );

        } catch (Exception e) {

            return null;

        }

    }

}
