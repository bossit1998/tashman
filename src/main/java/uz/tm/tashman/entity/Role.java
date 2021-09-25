package uz.tm.tashman.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "roles")
public class Role implements Serializable {

    private static final long serialVersionUID = -3597858031350917558L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

//	@Enumerated(EnumType.STRING)
//	@Column(length = 20)
    private String name;
}
