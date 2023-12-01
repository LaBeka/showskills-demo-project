package edu.example.demoproject.entities;

import lombok.*;

import javax.persistence.*;


@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@Entity
@Table(name="cart_item_entity")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name="cart_id")
    private Long cartId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int qty;
}