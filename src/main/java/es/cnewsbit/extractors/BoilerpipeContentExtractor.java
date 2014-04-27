package es.cnewsbit.extractors;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleSentencesExtractor;
import lombok.extern.log4j.Log4j2;

/**
 * Created by joshmahony on 27/04/2014.
 */
@Log4j2
public class BoilerpipeContentExtractor implements ContentExtractor {

    @Override
    public String extract(Extractable extractable) {

        String content = "";

        try {

            content = ArticleSentencesExtractor.INSTANCE.getText(extractable.getHtml());

        } catch (BoilerpipeProcessingException e) {

            log.debug(e.getMessage());

        }

        return content;

    }

}
