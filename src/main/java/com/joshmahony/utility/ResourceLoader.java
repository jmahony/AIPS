package com.joshmahony.utility;

import org.apache.commons.io.IOUtils;

/**
 * Created by josh on 02/03/14.
 */
public class ResourceLoader {

    /**
     *
     * Returns a resource as a string
     *
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
