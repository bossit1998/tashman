package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Sales implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Assortment assortment;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Client client;
    private Double amount;
    private LocalDateTime soldDate;

}
