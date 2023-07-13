package uz.tm.tashman.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class SalesRequestModel {
    private Long productId;
    private Long assortmentId;
    private Long clientId;
    private Double amount;
}
