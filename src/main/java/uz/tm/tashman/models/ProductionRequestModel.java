package uz.tm.tashman.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Unit;


@Setter
@Getter
@NoArgsConstructor
public class ProductionRequestModel {
    private Long productId;
    private Long assortmentId;
    private Integer quantity;
    private Unit unit;
}