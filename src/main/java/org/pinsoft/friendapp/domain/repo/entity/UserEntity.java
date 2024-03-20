package org.pinsoft.friendapp.domain.repo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "users")
@Entity
public class UserEntity {

    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false, name = "username", unique = true, columnDefinition = "VARCHAR(50)")
    private String username;
    @Column(nullable = false, name = "email", unique = true, columnDefinition = "VARCHAR(50)")
    private String email;
    @Column(nullable = false, name = "password")
    private String password;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "is_online", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isOnline;


    @OneToMany(mappedBy = Relationship_.USER_ONE, cascade = CascadeType.ALL)
    private List<Relationship> relationshipsUserOne;


    @OneToMany(mappedBy = Relationship_.USER_TWO, cascade = CascadeType.ALL)
    private List<Relationship> relationshipsUserTwo;


    @OneToMany(mappedBy = Relationship_.ACTION_USER, cascade = CascadeType.ALL)
    private List<Relationship> relationshipsActionUser;

    @OneToMany(mappedBy = Message_.FROM_USER, cascade = CascadeType.ALL)
    private List<Message> fromUserMessagesList;


    @OneToMany(mappedBy = Message_.TO_USER, cascade = CascadeType.ALL)
    private List<Message> toUserMessagesList;
}
