package es.cnewsbit.extractors;

public interface Smoothable {

    void setSmoothedValue(Double value);
    Double getSmoothedValue();
    Double getValueToSmooth();

}
