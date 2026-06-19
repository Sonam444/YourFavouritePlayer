package com.example.YourFavouritePlayer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

	private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

	@Value("${app.upload-dir:uploads}")
	private String uploadDir;

	public String store(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return null;
		}

		String contentType = file.getContentType();
		if (!StringUtils.hasText(contentType) || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
			throw new IllegalArgumentException("Only image files can be uploaded");
		}

		String originalFilename = file.getOriginalFilename();
		String extension = StringUtils.getFilenameExtension(originalFilename);
		if (!StringUtils.hasText(extension)) {
			throw new IllegalArgumentException("Image file must have an extension");
		}

		extension = extension.toLowerCase(Locale.ROOT);
		if (!ALLOWED_EXTENSIONS.contains(extension)) {
			throw new IllegalArgumentException("Allowed image types: jpg, jpeg, png, gif, webp");
		}

		try {
			Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
			Files.createDirectories(root);

			String filename = UUID.randomUUID() + "." + extension;
			Path destination = root.resolve(filename).normalize();
			if (!destination.startsWith(root)) {
				throw new IllegalArgumentException("Invalid image path");
			}

			file.transferTo(destination);
			return "/uploads/" + filename;
		} catch (IOException exception) {
			throw new IllegalStateException("Could not save uploaded image", exception);
		}
	}
}
