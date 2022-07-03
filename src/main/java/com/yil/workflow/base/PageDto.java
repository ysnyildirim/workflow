package com.yil.workflow.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private long totalElements;
    private int totalPages;
    private List<T> content;
    private int currentPage;

    public static <P, V> PageDto<V> toDto(Page<P> page, Function<P, V> function) {
        List<V> list = new ArrayList<>();
        page.getContent().forEach(f -> list.add(function.apply(f)));
        return new PageDtoBuilder<V>()
                .currentPage(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(list)
                .build();
    }

}
