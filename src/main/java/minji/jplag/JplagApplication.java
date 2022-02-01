package minji.jplag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Collections.reverse;

@SpringBootApplication
public class JplagApplication {

	public static void main(String[] args) throws IOException {

		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		SpringApplication.run(JplagApplication.class, args);

		//test
		List command = new ArrayList();
		//command.add("cmd");
		command.add("java");
		command.add("-jar");
		command.add("C:\\Users\\User\\Desktop\\MinjiKim\\jplag-3.0.0-jar-with-dependencies.jar");//jar파일위치
		command.add("C:\\Users\\User\\IdeaProjects\\springJplag\\codes");//test코드들이잇는 파일의 위치

		try{
			Process process = new ProcessBuilder(command).start();
			BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = outReader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
		e.printStackTrace();

		}


	}


}
