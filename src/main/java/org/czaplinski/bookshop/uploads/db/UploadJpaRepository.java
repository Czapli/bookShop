package org.czaplinski.bookshop.uploads.db;

import org.czaplinski.bookshop.uploads.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadJpaRepository extends JpaRepository<Upload, Long> {
}
