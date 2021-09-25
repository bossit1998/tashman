package uz.tm.tashman.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResPageable {
    private Object object;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;

    public ResPageable(Object object) {
        this.object = object;
    }

    public ResPageable(Object object, Long totalElements, Integer currentPage) {
        this.object = object;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
    }
}