package minji.jplag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Scanner;

import static java.util.Collections.reverse;

@SpringBootApplication
public class JplagApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(JplagApplication.class, args);

		//
		Process process = new ProcessBuilder("cmd", "cd", "C:/Users/user/Desktop/minji/원본jplag2/jplag/src/main/java/de").start();
		process.
	}


}
