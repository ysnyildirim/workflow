/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.base;

import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Mapper<S, T> {

    private final Function<S, T> function;

    public Mapper(@NotNull Function<S, T> function) {
        this.function = function;
    }

    public T map(S source) {
        return function.apply(source);
    }

    public List<T> map(@NotNull List<S> source) {
        List<T> list = new ArrayList<>();
        source.forEach(f -> list.add(function.apply(f)));
        return list;
    }

    public PageDto<T> map(@NotNull Page<S> page) {
        PageDto<T> pageDto = PageDto.toDto(page, function);
        return pageDto;
    }

}
