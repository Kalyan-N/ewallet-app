package com.pavan;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Configuration;
@Entity
@Getter
@Table(name = "app_user")
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String name;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String contact;


}
