package com.tinqinacademy.hotel.core.conversion;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;


public abstract class BaseConverter<S,T> implements Converter<S,T> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public T convert(S source) {

        log.info("Start convert input: {}", source);
        T result = convertObject(source);
        log.info("End convert result: {}", result);

        return result;
    }

    protected abstract T convertObject(S source);
}
