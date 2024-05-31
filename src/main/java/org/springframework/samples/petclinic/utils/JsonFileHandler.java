package org.springframework.samples.petclinic.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JsonFileHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public JsonFileHandler() {
		// Constructor vacío
	}

	public List<Map<String, Object>> readFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists() && !file.isDirectory()) {
			return objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {
			});
		}
		else {
			return new ArrayList<>();
		}
	}

	public void writeFile(List<Map<String, Object>> element, String filePath) throws IOException {
		objectMapper.writeValue(new File(filePath), element);
	}

	public Optional<Map<String, Object>> findById(Integer id, String filePath) throws IOException {
		List<Map<String, Object>> element = readFile(filePath);
		for (Map<String, Object> elements : element) {
			if (elements.get("id").equals(id)) {
				return Optional.of(elements);
			}
		}
		return Optional.empty();
	}

}
