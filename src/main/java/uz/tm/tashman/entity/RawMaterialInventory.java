package uz.tm.tashman.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class RawMaterialInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long rawMaterialId;
    private Long priceInUsd;
    private Long priceInUzs;
    private Long currencyRate;
    private String imageUrl;
    private LocalDateTime dateTime;
}
