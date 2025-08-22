package kr.elroy.aigoya.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import kr.elroy.aigoya.order.domain.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "dailyTarget")
    private Long dailyTarget;

    @Column(name = "nx")
    private Integer nx;

    @Column(name = "ny")
    private Integer ny;

    @OneToMany(mappedBy = "store", orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
}
