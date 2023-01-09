package org.czaplinski.bookshop.uploads.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.czaplinski.bookshop.jpa.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Upload extends BaseEntity {

    private byte[] file;
    private String contentType;
    private String fileName;
    @CreatedDate
    private LocalDateTime createdAt;

    public Upload(String fileName, String contentType, byte[] file) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.file = file;
    }
}
