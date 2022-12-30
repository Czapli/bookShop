package org.czaplinski.bookshop.uploads.application;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.czaplinski.bookshop.uploads.application.ports.UploadUseCase;
import org.czaplinski.bookshop.uploads.db.UploadJpaRepository;
import org.czaplinski.bookshop.uploads.domain.Upload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UploadService implements UploadUseCase {
    private final UploadJpaRepository repository;

    @Override
    public Upload save(SaveUploadCommand command) {
        Upload upload = new Upload(
                command.getFileName(),
                command.getContentType(),
                command.getFile()
        );
        repository.save(upload);
        System.out.println("Upload saved: " + upload.getFileName() + " with Id: " + upload.getId());
        return upload;
    }

    @Override
    public Optional<Upload> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void removeById(Long id) {
        repository.deleteById(id);
    }
}
