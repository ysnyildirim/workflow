package com.yil.workflow.base;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortOrderConverter implements Converter<String[], List<Sort.Order>> {

    private final String[] avaiableNames;

    public SortOrderConverter(String[] avaiableNames) {
        this.avaiableNames = avaiableNames;
    }

    @Override
    public List<Sort.Order> convert(String[] source) throws UnsupportedOperationException, IllegalArgumentException {
        List<Sort.Order> orders = new ArrayList<>();
        if (source == null)
            return orders;
        for (String order : source) {
            String[] sort = order.split(",");
            if (sort.length < 1 || sort.length > 2)
                throw new UnsupportedOperationException("Sort syntax error!");
            if (Arrays.stream(avaiableNames).noneMatch(f -> f.equalsIgnoreCase(sort[0].toLowerCase())))
                throw new UnsupportedOperationException("Sort not avaiable!");
            if (sort.length == 2 && !Arrays.asList("ASC", "DESC").contains(sort[1].toUpperCase()))
                throw new IllegalArgumentException("Sort direction not avaiable!");
            if (sort.length == 1)
                orders.add(new Sort.Order(Sort.Direction.ASC, sort[0]));
            else
                orders.add(new Sort.Order(Sort.Direction.fromString(sort[1]), sort[0]));
        }
        return orders;
    }

}