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

	public List<Map<String, Object>> readEmployees(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists() && !file.isDirectory()) {
			return objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {
			});
		}
		else {
			return new ArrayList<>();
		}
	}

	public void writeEmployees(List<Map<String, Object>> employees, String filePath) throws IOException {
		objectMapper.writeValue(new File(filePath), employees);
	}

	public Optional<Map<String, Object>> findById(Integer id, String filePath) throws IOException {
		List<Map<String, Object>> employees = readEmployees(filePath);
		for (Map<String, Object> employee : employees) {
			if (employee.get("id").equals(id)) {
				return Optional.of(employee);
			}
		}
		return Optional.empty();
	}

}
