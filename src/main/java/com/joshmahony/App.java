package com.joshmahony;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    private String[] tokenize(String sentence) {

        InputStream modelIn = null;

        String[] tokens = null;
        try {

            modelIn = getClass().getResourceAsStream("/en-token.bin");

            TokenizerModel model = new TokenizerModel(modelIn);

            Tokenizer tokenizer = new TokenizerME(model);

            tokens = tokenizer.tokenize(sentence);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                }
                catch (IOException e) {
                }
            }
        }

        return tokens;

    }

    private String[] sentanceExample(String document) {

        SentenceDetector _sentanceDetector = null;

        InputStream modelIn = null;

        try {

            modelIn = getClass().getResourceAsStream("/en-sent.bin");

            final SentenceModel sentenceModel = new SentenceModel(modelIn);

            modelIn.close();

            _sentanceDetector = new SentenceDetectorME(sentenceModel);

            String[] sen =_sentanceDetector.sentDetect(document);

            return sen;

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (modelIn != null) {


                try {
                    modelIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        return new String[0];

    }
}
