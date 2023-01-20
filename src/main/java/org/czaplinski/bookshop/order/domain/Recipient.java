package org.czaplinski.bookshop.order.domain;

import jakarta.persistence.Entity;
import lombok.*;
import org.czaplinski.bookshop.jpa.BaseEntity;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipient extends BaseEntity {
    private String email;
    private String name;
    private String phone;
    private String street;
    private String city;
    private String zipCode;

}
