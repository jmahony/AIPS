package com.joshmahony.processors;

import java.util.Set;

/**
 * Created by joshmahony on 13/03/2014.
 */
public interface NewsProcessable {

    public String getContents();

    public String getAuthor();

    public Set<String> getMetaKeywords();

    public String getMetaTitle();

}
