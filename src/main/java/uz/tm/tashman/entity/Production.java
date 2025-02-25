package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.tm.tashman.enums.Unit;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Production implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Assortment assortment;
    private LocalDateTime date;
    private Integer quantity;
    private Double boxCount;
    private Unit unit;
}
