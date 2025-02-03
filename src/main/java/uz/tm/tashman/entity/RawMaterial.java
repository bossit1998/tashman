package uz.tm.tashman.entity;

import javax.persistence.*;

@Entity
public class RawMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne
    private Producer producer;
    private String comment;
}
