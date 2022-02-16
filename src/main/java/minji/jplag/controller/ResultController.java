package minji.jplag.controller;

import lombok.extern.slf4j.Slf4j;
import minji.jplag.dto.AssignmentDto;
import minji.jplag.dto.SubjectDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ResultController {
    @GetMapping("/result")
    public String resultFuc(@RequestParam String assignmentName,
                            @RequestParam String subjectName, Model model){

        // subject/assingment 에 있는 code들을 바탕으로 코드 표절률검사
        //test
        List command = new ArrayList();
        //command.add("cmd");
        //cmd창으로 명령어 실행
        command.add("cmd");
        command.add("/c");
        command.add("java");
        command.add("-jar");
        command.add("./src/main/resources/static/jplag-3.0.0-jar-with-dependencies.jar");//jar파일위치
        //command.add("./src/main/resources/static/files/"+subjectName+"/" + assignmentName);//test코드들이잇는 파일의 위치
        command.add("./src/main/resources/static/files/"+subjectName+"/" + assignmentName + "/2018");//test코드들이잇는 파일의 위치
        List<String> resultLine = new ArrayList<>();
        String temp="";
        try{
            Process process = new ProcessBuilder(command).start();
            BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //명령어 실행한 결과를 받아서 출력하기
            while ((line = outReader.readLine()) != null) {
                resultLine.add(line);
                System.out.println(line);
                temp=line;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        String[] resultPath = temp.split(" ");

        model.addAttribute("results",resultLine);
        return "redirect:" + "http://www.naver.com";
    }
}
