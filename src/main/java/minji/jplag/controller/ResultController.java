package minji.jplag.controller;

import lombok.extern.slf4j.Slf4j;
import minji.jplag.dto.AssignmentDto;
import minji.jplag.dto.CodeDto;
import minji.jplag.dto.SubjectDto;
import minji.jplag.service.AssignmentService;
import minji.jplag.service.CodeService;
import minji.jplag.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ResultController {
    private static CodeService codeService;
    private static SubjectService subjectService;
    private static AssignmentService assignmentService;

    @Autowired
    public ResultController(CodeService codeService, SubjectService subjectService, AssignmentService assignmentService){
        this.codeService = codeService;
        this.subjectService = subjectService;
        this.assignmentService = assignmentService;
    }
    @GetMapping("/result")
    public String resultFuc(@RequestParam String assignmentName,
                            @RequestParam String subjectName, Model model) throws IOException {

        String allCode = "all";

        // subject/assingment 에 있는 code들을 바탕으로 코드 표절률검사
        //test
        List command = new ArrayList();
        //command.add("cmd");
        //cmd창으로 명령어 실행

        command.add("cmd");

        command.add("/c");
        command.add("java");
        command.add("-jar");
        command.add("C:\\Users\\jplag-3.0.0-jar-with-dependencies.jar");//jar파일위치
        //command.add("./src/main/resources/static/files/"+subjectName+"/" + assignmentName);//test코드들이잇는 파일의 위치
        command.add("C:\\Users\\" +subjectName+"\\" + assignmentName + "\\" + allCode);//test코드들이잇는 파일의 위치


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

        model.addAttribute("results", resultLine);


        List<SubjectDto> subjectDtoList = subjectService.getFiles();
        List<AssignmentDto> assignmentDtoList = assignmentService.getFiles();
        List<CodeDto> codeDtoList = codeService.getFiles();
        model.addAttribute("subjects", subjectDtoList);
        model.addAttribute("assignments", assignmentDtoList);
        model.addAttribute("codes", codeDtoList);

        //return resultPath[3] + "\\main.html";
        //return "ddd";
        //return "index";


        //result 하위파일들의 이름을 모두 뽑아서...
        String[] subfileList = new File(resultPath[3]).list();

        if (subfileList==null){
            return "/jplagError.html";
        }
        for(int i=0;i<subfileList.length;i++){
            copy(resultPath[3], subfileList[i]);

        }

        return "/index.html";
        //return htmlCopy(resultPath[3] + "/main.html");
    }
    public void copy(String path, String fileName) throws IOException {
        String[] temp = path.split("\\\\");
        int idx = temp.length -2;//
        String copyfilePath ="";
        for(int i =0;i<idx;i++){
            copyfilePath += temp[i] + "/";
        }
        copyfilePath += "springJplag\\src\\main\\resources\\templates\\" + fileName;


        //만약에 .html로 끝나면 htmlcopy로 보내버린다
        String[] extension = fileName.split("\\.");
        if(extension[1].equals("html")){
            htmlCopy(path, fileName);
            return;
        }

        // 1. 원본 File, 복사할 File 준비
        File file = new File(path + "/" + fileName);
        File newFile = new File(copyfilePath);
        // 2. FileInputStream, FileOutputStream 준비
        FileInputStream input = new FileInputStream(file);
        FileOutputStream output = new FileOutputStream(newFile);
        // 3. 한번에 read하고, write할 사이즈 지정
        byte[] buf = new byte[1024];
        // 4. buf 사이즈만큼 input에서 데이터를 읽어서, output에 쓴다.
        int readData;

        //첫번째 순간만 검사할 변수
        boolean flag = true;
        while ((readData = input.read(buf)) > 0) {
            output.write(buf, 0, readData);
        }
        // 5. Stream close
        input.close();
        output.close();

    }
    public String htmlCopy(String resultPath, String fileName) throws IOException {
        //resultpath를 가지고 절대경로 만들기
        String[] temp = resultPath.split("\\\\");
        int idx = temp.length -2;//
        String copyfilePath ="";
        for(int i =0;i<idx;i++){
            copyfilePath += temp[i] + "/";
        }
        copyfilePath += "springJplag\\src\\main\\resources\\templates\\" + fileName;

        System.out.println("copyfilePath!!!!!!!!!!!!!!!!" + copyfilePath );

        // 1. 원본 File, 복사할 File 준비
        File file = new File(resultPath + "\\" +fileName);
        File newFile = new File(copyfilePath);
        // 2. FileInputStream, FileOutputStream 준비
        FileInputStream input = new FileInputStream(file);
        FileOutputStream output = new FileOutputStream(newFile);
        // 3. 한번에 read하고, write할 사이즈 지정
        byte[] buf = new byte[1024];
        // 4. buf 사이즈만큼 input에서 데이터를 읽어서, output에 쓴다.
        int readData;

        //첫번째 순간만 검사할 변수
        boolean flag = true;
        //html파일을 읽는다
        while ((readData = input.read(buf)) > 0) {
            if(flag){
                //입력으로 들어오는 html은 형식이 일정하기때문에 다음과 같은 문자열로 나누게 되면 저 문자열을 기준으로 앞뒤 두개의 문자열로 쪼개진다
                //처음 입력이 들어왔을경우에만 이 동작을 수행한다. 저 문자열뒤에 "http://www.w3.org/TR/html4/loose.dtd" 을 출력해서 넣어줘야한다
                String[] list = new String(buf).split("\"-//W3C//DTD HTML 4.01 Transitional//EN\"");

                String arr = "";
                for(int i=0;i<list.length;i++){
                    //일단 list의 앞부분 즉, "-//W3C//DTD HTML 4.01 Transitional//EN" 문자열 앞부분을 arr에 더해준다
                    arr+=list[i];
                    //그리고 "-//W3C//DTD HTML 4.01 Transitional//EN"문자열 기준으로 앞뒤로잘렸으니까 그 문자열을 붙여주고
                    //붙이기로 결정한 "http://www.w3.org/TR/html4/loose.dtd"문자열도 붙여준다
                    if(i==0){
                        //쌍따옴표를 출력하기 위해서 이스케이프 사용
                        arr += " \"-//W3C//DTD HTML 4.01 Transitional//EN\"";
                        arr += " \"http://www.w3.org/TR/html4/loose.dtd\"";
                    }
                }

                //근데 이렇게 중간에 배열을 넣게 되면 while조건문에서의 readData(내가 얼마만큼 데이터를 읽었는지에 대한 값)과
                //output.write할때 넘겨주는 readData값이 달라지게된다.
                //이거 저위의  "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd"이 배열에 대해
                //.getbytes().length하면 구할 수있나?
                //buf에다가 넣으면 1024를 넘어서 데이터가 잘림

                String temp1 = " \"-//W3C//DTD HTML 4.01 Transitional//EN\"";
                String temp2 = " \"http://www.w3.org/TR/html4/loose.dtd\"";

                byte[] tempbuf = arr.getBytes();

                flag = false;
                //tempbuf에 저장되어있는 값에서 0부터 최종 완성본?인 arr의 길이만큼을 입력한다
                output.write(tempbuf, 0, arr.getBytes().length);
                continue;
            }
            output.write(buf, 0, readData);
        }
        // 5. Stream close
        input.close();
        output.close();

        return "result/main.html";

    }
}
