package com.joshmahony;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        new App();
        
    }
    
    public App() {

        try {
            String test = IOUtils.toString(
                this.getClass().getResourceAsStream("/documents/blogs-ouch-26193704"),
                "UTF-8"
            );
            
            HTMLDocument htmlDocument = new HTMLDocument(test);
            
            System.out.println("done");
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
