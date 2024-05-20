package com.queue.client.Queue.Client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Scanner;

@SpringBootApplication
public class QueueClientApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(QueueClientApplication.class);

		System.out.println("Por favor, ingresa el puerto en el que deseas ejecutar la aplicación: ");
		Scanner scanner = new Scanner(System.in);
		String puerto = scanner.nextLine();
		scanner.close();

		if (!subscribeToService(puerto)) {
			System.out.println("Falló la suscripción al servicio, se cancela la inicialización");
			System.exit(1);
		}

		app.setDefaultProperties(Collections.singletonMap("server.port", puerto));
		app.run(args);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			unsuscribe(puerto);
		}));
	}

	private static void unsuscribe(String puerto) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/unsuscribe"))
				.header("Content-Type", "text/plain")
				.POST(HttpRequest.BodyPublishers.ofString(String.format("http://localhost:%s/message", puerto)))
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println("Unsuscribing...");
		} catch (IOException | InterruptedException e) {
			System.out.println("Error haciendo el request, revise que el servicio se encuentre disponible: " + e.getMessage());
		}
	}

	private static boolean subscribeToService(String puerto) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/suscribe"))
				.header("Content-Type", "text/plain")
				.POST(HttpRequest.BodyPublishers.ofString(String.format("http://localhost:%s/message", puerto)))
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			int statusCode = response.statusCode();
			if (statusCode >= 200 && statusCode < 300) {
				String responseBody = response.body();
				System.out.println("Respuesta del servidor: " + responseBody);
				return responseBody.equals("suscribed correctly!!!");
			} else {
				System.out.println("La solicitud al servidor no fue exitosa. Código de estado: " + statusCode);
				return false;
			}
		} catch (IOException | InterruptedException e) {
			System.out.println(String.valueOf(e));
			return false;
		}
	}
}
