package com.deador.restshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    public void revoke() {
        user.setRefreshToken(null);
        setUser(null);
    }
}
