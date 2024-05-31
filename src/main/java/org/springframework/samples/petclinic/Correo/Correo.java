package org.springframework.samples.petclinic.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.samples.petclinic.utils.JsonFileHandler;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.mail.*;
import javax.mail.internet.*;

@RestController
@RequestMapping("/api/correo")
class Correo {

	private final String filePath = "src/main/resources/db/JSON/Employees.json";

	@Autowired
	private JsonFileHandler jsonFileHandler;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	// Envia un correo electronico
	private String sendEmail(String email, String subject, String body) {
		try {

			Properties properties = new Properties();
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");

			// Crear una sesión de correo autenticada
			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("yeninusis@gmail.com", "jnih tmkd cpeq rsjj ");
				}
			});

			// Crear un mensaje de correo electrónico
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("yeninusis@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(subject);
			message.setContent(body, "text/html");

			// Enviar el mensaje
			Transport.send(message);
			return "Correo enviado";

		}
		catch (MessagingException e) {
			// Manejo de la excepción
			for (int i = 0; i < 50; i++) {
				System.out.println();
			}
			e.printStackTrace(); // O cualquier otra acción que desees
			return "Error al enviar el correo";
		}
		// Configurar propiedades para la sesión de correo
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> sendCorreo(@Valid @RequestBody Map<String, Object> usuario) {
		try {
			List<Map<String, Object>> employees = jsonFileHandler.readFile(filePath);
			// Envia un correo electronico
			// Nombre, apellido, cargo y id
			Integer id = (Integer) usuario.get("id");
			Optional<Map<String, Object>> employee = jsonFileHandler.findById(id, filePath);
			if (employee.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
			}
			String email = "santiago.murillo@utp.edu.co";
			String name = employee.get().get("first_name").toString();
			String lastName = employee.get().get("last_name").toString();
			String cargo = employee.get().get("cargo").toString();

			// lee el template del correo el cual esta en
			// "src\main\resources\templates\correo.html"
			String template = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/correo.html")));
			// reemplaza los valores del template por los valores del usuario
			template = template.replace("{{name}}", name);
			template = template.replace("{{lastName}}", lastName);
			template = template.replace("{{cargo}}", cargo);
			// envia el correo
			String subject = "Inscripción a seguridad social";
			String respuesta = sendEmail(email, subject, template);
			return ResponseEntity.ok(Map.of("message", respuesta));
		}
		catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error writing to file", e);
		}
	}

}
