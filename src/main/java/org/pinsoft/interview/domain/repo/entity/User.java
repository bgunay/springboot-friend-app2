package org.pinsoft.interview.domain.repo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class User extends BaseEntity {

    @Builder
    public User(String username, String email, String password, String firstName, String lastName, String address, String city, boolean isOnline, List<Relationship> relationshipsUserOne, List<Relationship> relationshipsUserTwo, List<Relationship> relationshipsActionUser, List<Message> fromUserMessagesList, List<Message> toUserMessagesList) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.isOnline = isOnline;
        this.relationshipsUserOne = relationshipsUserOne;
        this.relationshipsUserTwo = relationshipsUserTwo;
        this.relationshipsActionUser = relationshipsActionUser;
        this.fromUserMessagesList = fromUserMessagesList;
        this.toUserMessagesList = toUserMessagesList;
    }

    @Column(nullable = false, name = "username", unique = true, columnDefinition = "VARCHAR(50) BINARY")
    private String username;
    @Column(nullable = false, name = "email", unique = true, columnDefinition = "VARCHAR(50) BINARY")
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

    @OneToMany(mappedBy = "userOne", cascade = CascadeType.ALL)
    private List<Relationship> relationshipsUserOne;
    @OneToMany(mappedBy = "userTwo", cascade = CascadeType.ALL)
    private List<Relationship> relationshipsUserTwo;
    @OneToMany(mappedBy = "actionUser", cascade = CascadeType.ALL)
    private List<Relationship> relationshipsActionUser;

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL)
    private List<Message> fromUserMessagesList;
    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL)
    private List<Message> toUserMessagesList;
}
