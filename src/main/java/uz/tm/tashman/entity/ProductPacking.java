package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Unit;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductPacking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long productId;
    private Integer piecesPerPackage;
    private Double packageNettoWeight;
    private Double packageBruttoWeight;
    private String packageDimensions;
    private Double volume;
    @Enumerated(EnumType.STRING)
    private Unit unit;
    private Double boxQuantity;
}