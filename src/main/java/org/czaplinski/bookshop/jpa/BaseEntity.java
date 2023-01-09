package org.czaplinski.bookshop.jpa;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Version;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(of = "uuid")
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String uuid = UUID.randomUUID().toString();
    @Version
    private long version;

}
