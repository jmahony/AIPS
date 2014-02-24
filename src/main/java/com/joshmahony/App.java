package com.joshmahony;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

        ProcessingQueue<String> pq = new ProcessingQueue<String>();

        ArrayList<NewsArticle> processed = new ArrayList<NewsArticle>();

        while (!pq.queue.isEmpty()) {

            NewsArticle na = new NewsArticle(pq.poll());

            processed.add(na);

        }

        String d = "European Commission President Jose Manuel Barroso has said it would be \"extremely difficult, if not impossible\" for an independent Scotland to join the European Union.\n" +
                "\n" +
                "Speaking to the BBC's Andrew Marr he said an independent Scotland would have to apply for membership and get the approval of all current member states.\n" +
                "\n" +
                "Scotland's Finance Minister described his comments as \"pretty preposterous\".\n" +
                "\n" +
                "John Swinney said Mr Barroso's view was based on a false comparison.\n" +
                "\n" +
                "The referendum on Scottish independence will be held on 18 September, with voters being asked the Yes/No question: \"Should Scotland be an independent country?\"\n" +
                "\n" +
                "New state\n" +
                "In his interview with Andrew Marr, Mr Barroso said: \"In case there is a new country, a new state, coming out of a current member state it will have to apply.\"\n" +
                "\n" +
                "\n" +
                "John Swinney described Mr Barroso's comments as \"pretty preposterous\"\n" +
                "He said it was important that \"accession to the European Union will have to be approved by all other member states of the European Union.\"\n" +
                "\n" +
                "He went on: \"Of course it will be extremely difficult to get the approval of all the other member states to have a new member coming from one member state.\"\n" +
                "\n" +
                "Mr Barroso cited the example of the Spanish not recognising Kosovo.\n" +
                "\n" +
                "He said: \"We have seen Spain has been opposing even the recognition of Kosovo, for instance. So it is to some extent a similar case because it's a new country and so I believe it's going to be extremely difficult, if not impossible, a new member state coming out of our countries getting the agreement of the others.\"\n" +
                "\n" +
                "However, Mr Barroso made clear that it was up to the people of Scotland to decide their future, and he said he did not want to interfere in that process.\n" +
                "\n" +
                "In its White Paper on independence, launched in November, the Scottish government said the country would look to gain membership through Article 48 of the Treaty of the European Union.\n" +
                "\n" +
                "It said such a move could be achieved within 18 months of a \"Yes\" vote.\n" +
                "\n" +
                "'Agreed process'\n" +
                "\n" +
                "Speaking on the BBC's Sunday Politics programme, Mr Swinney said: \"I think President Barroso's remarks are pretty preposterous.\n" +
                "\n" +
                "\"He's set out his position linking and comparing Scotland to the situation in Kosovo.\n" +
                "\n" +
                "\n" +
                "\"Scotland has been a member of the EU for 40 years - we're already part of the European Union.\"\n" +
                "\n" +
                "Mr Swinney said there was no indication any member state would veto Scotland's membership, including Spain where Catalan separatists are pushing for independence.\n" +
                "\n" +
                "He added: \"The Spanish Foreign Minister said if there is an agreed process within the United Kingdom by which Scotland becomes an independent country then Spain has nothing to say about the whole issue.\n" +
                "\n" +
                "\"That indicates to me quite clearly that the Spanish government will have no stance to take on the question of Scottish membership of the European Union.\"\n" +
                "\n" +
                "Mr Swinney also denied Scotland would have to join the euro if it became a member of the EU in its own right.\n" +
                "\n" +
                "He said to adopt the euro, countries first had to be a member of the exchange rate mechanism and Scotland had \"no intention\" of signing up.\n" +
                "\n" +
                "'Lack of clarity'\n" +
                "When asked in a BBC interview for his response to the European Commission president's comments, Labour leader Ed Miliband said he thought recent developments on issues like EU membership and currency showed: \"Alex Salmond's case is coming apart\".\n" +
                "\n" +
                "He said: \"I think we can give people greater hope for Scotland within the United Kingdom ensuring that we make ourselves a socially just country, rather than all the risks associated with Scotland going it alone.\"\n" +
                "\n" +
                "Former chancellor and chairman of the pro-Union Better Together campaign, Alistair Darling, said the \"wheels had begun to fall off the wagon\" of the nationalists' campaign.\n" +
                "\n" +
                "He added: \"You now see that Alex Salmond is a man without a plan.\n" +
                "\n" +
                "\"Both the lack of clarity on the currency, as well as on Europe, means that Scotland would be taking a wholly unnecessary and undesirable risk if it were to vote to separate this autumn.\"\n" +
                "\n" +
                "\n" +
                "On Monday, Scotland's First Minister Alex Salmond is due to address pro-independence business leaders in Aberdeen.\n" +
                "\n" +
                "He has vowed to \"deconstruct\" the chancellor's case against a currency union in a speech at the Business for Scotland event.\n" +
                "\n" +
                "He will say George Osborne's position that a vote for independence would mean walking away from the pound, is \"ill-thought out and misinformed\".\n" +
                "\n" +
                "Other SNP politicians have also dismissed Mr Barroso's remarks. Angus MacNeil, MP for the Western Isles, told the BBC he thought the comments were \"nonsense\".\n" +
                "\n" +
                "He said: \"Barroso saying that the EU is not a club for members or nations who want to be a member of it is probably a very new and revolutionary step.\n" +
                "\n" +
                "\"It is however I think Barroso at form playing more politics and it'll have the same effect as George Osborne - it'll help Scotland.\"";

        //namedEntityRecognitionExample(sentanceExample(d));

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

    private void namedEntityRecognitionExample(String[] sentences) {

        InputStream modelIn = null;

        try {

            modelIn = getClass().getResourceAsStream("/en-ner-person.bin");

            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);

            modelIn.close();

            NameFinderME nameFinder = new NameFinderME(model);

            for (String sentence : sentences) {

                String[] tokens = tokenize(sentence);

                Span[] namedEntities = nameFinder.find(tokens);

                for (Span entity : namedEntities) {
                    System.out.println("start: " + entity.getStart() + ". end: " + entity.getEnd());
                    for (int i = entity.getStart(); i < entity.getEnd(); i++) {
                        System.out.print(tokens[i] + " ");
                    }
                    System.out.println();
                }

            }

            nameFinder.clearAdaptiveData();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                }
                catch (IOException e) {
                }
            }
        }

    }
}
