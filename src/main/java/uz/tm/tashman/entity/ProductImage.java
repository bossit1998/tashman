package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String imageUrl;
    private String thumbnailImageUrl;

    private Integer sortOrder;


    @Column(columnDefinition = "boolean default false")
    private Boolean isVisible = false;
    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = false;
    @Column(columnDefinition = "boolean default false")
    private Boolean isInProduction = false;

    private LocalDateTime createdDate = LocalDateTime.now();
    private Long createdBy;
    private LocalDateTime deletedDate;
    private Long deletedBy;
}