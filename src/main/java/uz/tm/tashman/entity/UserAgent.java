package uz.tm.tashman.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_agent")
@Entity
public class UserAgent implements Serializable {

    private static final long serialVersionUID = 9084968307271779096L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userAgent;

    private boolean isVerified = false;

    private LocalDateTime tokenDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private boolean deletedStatus = false;

    private LocalDateTime deletedDate;

}