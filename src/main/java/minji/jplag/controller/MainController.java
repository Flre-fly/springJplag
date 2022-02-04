package minji.jplag.controller;

import lombok.extern.slf4j.Slf4j;
import minji.jplag.domain.entity.Code;
import minji.jplag.dto.SubjectDto;
import minji.jplag.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.ZipFile;

@Controller
@Slf4j
public class MainController {
    private SubjectService subjectService;

    public MainController(SubjectService subjectService) throws IOException {
        this.subjectService = subjectService;
    }
    @GetMapping("/")
    public String defaultFunc(Model model){
        //디비에서 file을 꺼내서 model에 넘겨준다
        model.addAttribute("files", subjectService.getFiles());

        return "index";
    }
    //RequestMapping이란
    //클라이언트의 요청을 처리할 메서드구현
    @PostMapping("/upload")
    public String fileupload(@RequestParam MultipartFile uploadfile,
                             Model model) throws IllegalStateException, IOException {

        //String fileDir = new ClassPathResource("/static/files").getFile().getAbsolutePath();

        //그냥 버튼만 누르는 경우 zip파일 안만들고 그냥 return하기
        if(uploadfile.isEmpty()) return "uploadResult";

        //userdir와 뒤에있는 path를 결합하여 하나의 path로 만드는과정인거임
        String fileDir = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/files").toString();

        //이거 이름같을경우 예외처리도 해줘야함
        //확장자 제거
        int Idx = uploadfile.getOriginalFilename().lastIndexOf(".");
        String filename = uploadfile.getOriginalFilename().substring(0, Idx);
        String subjectPath = fileDir + "\\" + filename;


        //압축파일 넘어온것까진 괜찮은데.. 이제..압축파일을 풀어야함
        System.out.println(uploadfile.getOriginalFilename());
        //String despath = new ClassPathResource(fullPath).getFile().getAbsolutePath();
        //압축파일이 multipartfile의 경로..
        String beforeFilePath = fileDir +"\\"+ uploadfile.getOriginalFilename();
        //fullpath에 해당하는 곳에 dir를 만들ㄹ어준다
        new File(subjectPath).mkdir();
        //
        File beforeZipFile = new File(beforeFilePath);
        uploadfile.transferTo(new File(beforeFilePath));

        extractZipFiles(beforeZipFile,subjectPath);


        beforeZipFile.delete();

        //압축을 풀었으니 이제 subject 하위의 assignment나 code나 codebyyear에 대해 만들어줘야함



        List<Code> codes = makeCode(subjectPath);
        //code를 subjectdto에 넣어준다

        SubjectDto file = SubjectDto.builder()
                .filename(filename)
                .filePath(subjectPath)
                .codes(codes)
                .build();

        //uploadfile.transferTo(new Subject(fullPath));//fileDir에 파일 저장, 파일에 대한 경로는 fullPath

        //나중에 추가
        //subjectService.saveFile(file);

        model.addAttribute("files", file);//files로 추가했으니 uploadResult.html에거 files로 찾을 수 있어


        return "uploadResult";
    }

    public static List<Code> makeCode(String subjectPath) throws UnsupportedEncodingException {
        File subject = new File(subjectPath);
        File[] fList = subject.listFiles();

      //  System.out.println(codeName[0]);
        List<Code> codes = new ArrayList<>();
        for(int i=0;i<subject.list().length;i++){
            byte[] euckrStringBuffer  = fList[i].getName().getBytes(Charset.forName("euc-kr"));
            String codeName = new String(euckrStringBuffer, "euc-kr");

            String [] charSet = {"utf-8","euc-kr","ksc5601","iso-8859-1","x-windows-949"};

            for (int m=0; m<charSet.length; m++) {
                for (int j=0; j<charSet.length; j++) {
                    try {
                        System.out.println("[" + charSet[i] +"," + charSet[j] +"] = " + new String(fList[i].getName().getBytes(charSet[i]), charSet[j]));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            String codePath = "\\" + subjectPath + "\\" + codeName;
            String[] info = codeName.split("\\_");
            System.out.println(info[i]);
            Code code = Code.builder()
                    .year(info[0])
                    .assignmentNum(info[1])
                    .studentNum(info[2])
                    .studentName(info[3])
                    .filePath(codePath).build();
            codes.add(code);
        }
        return codes;
    }

    public static void extractZipFiles(File source, String targetDir) throws IOException {
        ZipFile zipFile = new ZipFile(source);
        zipFile.extractAll(targetDir);

    }


}
