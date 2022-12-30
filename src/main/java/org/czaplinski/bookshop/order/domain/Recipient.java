package org.czaplinski.bookshop.order.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipient {
    @Id@GeneratedValue
    private Long id;
    private String name;
    private String phone;
    private String street;
    private String city;
    private String zipCode;
    private String email;

    public Recipient(String name, String phone, String street, String city, String zipCode, String email) {

    }
}
