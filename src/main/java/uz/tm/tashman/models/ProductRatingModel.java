package uz.tm.tashman.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.entity.Product;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRatingModel {
    private Long id;
    private String ratingFor;
    private Product product;
    private String userNickname;
    private String review;
    private Double rating;
    private LocalDateTime createdDate;
    private boolean deleted;
    private LocalDateTime deletedDate;
}