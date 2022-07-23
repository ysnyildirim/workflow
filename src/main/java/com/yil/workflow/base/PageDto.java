package com.yil.workflow.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.function.Function;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private long totalElements;
    private int totalPages;
    private T[] content;
    private int currentPage;

    public static <P, V> PageDto<V> toDto(Page<P> page, Function<P, V> function) {
        Object[] elements = new Object[page.getContent().size()];
        for (int i = 0; i < page.getContent().size(); i++)
            elements[i] = function.apply(page.getContent().get(i));
        return new PageDtoBuilder<V>()
                .currentPage(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content((V[]) elements)
                .build();
    }

}
