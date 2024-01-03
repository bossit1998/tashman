package uz.tm.tashman.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductPackingModel {
    private Integer piecesPerPackage;
    private Double packageNettoWeight;
    private Double packageBruttoWeight;
    private String packageDimensions;
    private Double volume;
    private String volumeUnit;
    private Double boxQuantity;
}