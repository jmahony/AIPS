package es.cnewsbit.extractors;

/**
 * Created by joshmahony on 27/04/2014.
 */
public interface Smoothable {

    void setSmoothedValue(Double value);
    Double getSmoothedValue();
    Double getValueToSmooth();

}
