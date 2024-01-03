package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ProductInventoryHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    private LocalDate date;
    private Long productId;
    private Long assortmentId;
    private Long packingId;
}