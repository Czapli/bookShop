package org.czaplinski.bookshop.uploads.application.ports;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.czaplinski.bookshop.uploads.domain.Upload;

import java.util.Optional;


public interface UploadUseCase {
    Upload save(SaveUploadCommand command);
    Optional<Upload> getById(Long id);

    void removeById(Long coverId);


    @Value
    @AllArgsConstructor
    class SaveUploadCommand {
        String fileName;
        byte[] file;
        String contentType;

    }
}
