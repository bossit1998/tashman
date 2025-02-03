package uz.tm.tashman.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
public class Logs implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "action", columnDefinition = "TEXT")
    private String action;

    private LocalDateTime createdDate;

    public Logs(String message) {
        this.action = message;
        this.createdDate = LocalDateTime.now();
    }
}