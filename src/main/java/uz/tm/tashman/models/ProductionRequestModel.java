package uz.tm.tashman.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.VolumeUnit;


@Setter
@Getter
@NoArgsConstructor
public class ProductionRequestModel {
    private Long productId;
    private Long assortmentId;
    private Integer quantity;
    private VolumeUnit volumeUnit;
}
