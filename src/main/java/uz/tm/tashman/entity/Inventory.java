package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Unit;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Product product;
    private Double quantity;
    private Unit quantity_unit;
    private LocalDateTime lastUpdatedDate;
    private Long assortmentId;
    private Long packingId;
}