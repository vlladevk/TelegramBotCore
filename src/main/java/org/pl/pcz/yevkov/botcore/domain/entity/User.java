package org.pl.pcz.yevkov.botcore.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString(exclude = "userChats")
public class User implements BaseEntity<Long> {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private String userName;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<UserChat> userChats = new ArrayList<>();

}
