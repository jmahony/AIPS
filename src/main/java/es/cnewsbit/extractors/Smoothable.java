package es.cnewsbit.extractors;

/**
 * Interface for Smoothable objects
 */
public interface Smoothable {

    void setSmoothedValue(Double value);
    Double getSmoothedValue();
    Double getValueToSmooth();

}
