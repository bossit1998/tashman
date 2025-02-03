package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Unit;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "product_id, assortment_id")})
public class CurrentProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private Unit unit;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    private Assortment assortment;
    private Long packingId;

}
