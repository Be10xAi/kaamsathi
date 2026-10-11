package com.kaamsaathi.service;

import com.kaamsaathi.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Stores uploads OUTSIDE the web root with random file names. Validates extension, size and the real
 * file signature (magic bytes), so a renamed .exe/.html cannot be uploaded as an image.
 */
@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload.dir}") String dir) throws IOException {
        this.root = Path.of(dir).toAbsolutePath().normalize();
        Files.createDirectories(root);
    }

    public String store(MultipartFile file, Set<String> allowedExt, long maxBytes) {
        if (file == null || file.isEmpty()) return null;
        if (file.getSize() > maxBytes) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "फाइल बहुत बड़ी है।");
        }
        String ext = extension(file.getOriginalFilename());
        if (!allowedExt.contains(ext)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "यह फाइल प्रकार स्वीकार नहीं है।");
        }
        try {
            byte[] head;
            try (InputStream in = file.getInputStream()) { head = in.readNBytes(12); }
            if (!signatureMatches(ext, head)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "फाइल मान्य नहीं है।");
            }
            String name = UUID.randomUUID() + "." + ext;
            Path target = root.resolve(name).normalize();
            if (!target.startsWith(root)) throw new ApiException(HttpStatus.BAD_REQUEST, "फाइल मान्य नहीं है।");
            try (InputStream in = file.getInputStream()) { Files.copy(in, target); }
            return name;
        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "फाइल सेव नहीं हो सकी।");
        }
    }

    private static String extension(String filename) {
        if (filename == null) return "";
        int i = filename.lastIndexOf('.');
        return i < 0 ? "" : filename.substring(i + 1).toLowerCase(Locale.ROOT);
    }

    private static boolean startsWith(byte[] b, int... sig) {
        if (b.length < sig.length) return false;
        for (int i = 0; i < sig.length; i++) if ((b[i] & 0xFF) != sig[i]) return false;
        return true;
    }

    private static boolean signatureMatches(String ext, byte[] b) {
        return switch (ext) {
            case "jpg", "jpeg" -> startsWith(b, 0xFF, 0xD8, 0xFF);
            case "png" -> startsWith(b, 0x89, 0x50, 0x4E, 0x47);
            case "pdf" -> startsWith(b, 0x25, 0x50, 0x44, 0x46);
            case "webp" -> startsWith(b, 0x52, 0x49, 0x46, 0x46) && b.length >= 12
                    && b[8] == 'W' && b[9] == 'E' && b[10] == 'B' && b[11] == 'P';
            default -> false;
        };
    }
}
