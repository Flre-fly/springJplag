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
    public String resultFuc(@RequestParam String jarFilePath, @RequestParam String codePath, Model model){

        // subject/assingment 에 있는 code들을 바탕으로 코드 표절률검사
        //test
        List command = new ArrayList();
        //command.add("cmd");
        command.add("java");
        command.add("-jar");
        command.add(jarFilePath);//jar파일위치
        command.add(codePath);//test코드들이잇는 파일의 위치
        StringBuffer resultLine = new StringBuffer();
        try{
            Process process = new ProcessBuilder(command).start();
            BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = outReader.readLine()) != null) {
                resultLine.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        model.addAttribute("result", resultLine);
        return "result";
    }
}
