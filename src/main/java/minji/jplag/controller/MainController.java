package minji.jplag.controller;

import lombok.extern.slf4j.Slf4j;
import minji.jplag.dto.AssignmentDto;
import minji.jplag.dto.CodeDto;
import minji.jplag.dto.SubjectDto;
import minji.jplag.repository.AssignmentRepository;
import minji.jplag.repository.SubjectRepository;
import minji.jplag.service.AssignmentService;
import minji.jplag.service.CodeService;
import minji.jplag.service.SubjectService;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class MainController {
    private static CodeService codeService;
    private static SubjectService subjectService;
    private static AssignmentService assignmentService;
    //test용
    private static SubjectRepository subjectRepository;
    private static AssignmentRepository assignmentRepository;

    @Autowired//생성자의 인자로 들어오는 변수들에 의존관계를 자동으로 주입해준다
    public MainController(CodeService codeService,
                          SubjectService subjectService,
                          AssignmentService assignmentService,
                          SubjectRepository subjectRepository,
                          AssignmentRepository assignmentRepository){
        this.assignmentRepository = assignmentRepository;
        this.codeService = codeService;
        this.subjectService = subjectService;
        this.assignmentService = assignmentService;
        this.subjectRepository = subjectRepository;
        //나중에 이부분 삭제 repository부분

    }
    @GetMapping("/")
    public String mainpage(Model model){
        List<SubjectDto> subjectDtoList = subjectService.getFiles();
        List<AssignmentDto> assignmentDtoList = assignmentService.getFiles();
        List<CodeDto> codeDtoList = codeService.getFiles();
        model.addAttribute("subjects", subjectDtoList);
        model.addAttribute("assignments", assignmentDtoList);
        model.addAttribute("codes", codeDtoList);
        return "index";
    }

    //RequestMapping이란
    //클라이언트의 요청을 처리할 메서드구현
    @PostMapping("/upload")
    public String fileupload(@RequestParam MultipartFile uploadfile,
                             Model model) throws Throwable {

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

        //String despath = new ClassPathResource(fullPath).getFile().getAbsolutePath();
        //압축파일이 multipartfile의 경로..
        String beforeFilePath = fileDir +"\\"+ uploadfile.getOriginalFilename();
        //fullpath에 해당하는 곳에 dir를 만들ㄹ어준다
        //new File(subjectPath).mkdir();
        //
        File beforeZipFile = new File(beforeFilePath);
        //파일을 생성하였다
        File newfile = new File(beforeFilePath);
        //newfile을 생성한다 경로 : beforefilepath
        uploadfile.transferTo(newfile);

        //압축해제 filedir라는 dir를 만들고, 뒤의 인자는 압축대상파일이다
        decompress(new File(fileDir), beforeZipFile);
        //압축된파일을 지운다
        beforeZipFile.delete();
        //압축을 풀었으니 이제 subject 하위의 assignment나 code나 codebyyear에 대해 만들어줘야함

        //code에 대한 entity를 만들어 넘긴다
        makeCodeDto(subjectPath, filename, model);

        return "index2";
    }
    public static void makeCodeDto(String subjectPath, String subjectName, Model model) throws UnsupportedEncodingException {

        //db에서 모두 꺼내서 이름이 같은지를 확인하는 작업이 있어야함
        //그리고 이름이 같다면 새로 생성하지 말고 내용만 변경하도록(대체되도록)
        //예를들어 과목명이 a인데 같은 과목명의 파일이 들어온다면 새로들어온파일로 기존것을 대체하도록
        List<SubjectDto> subjectDtoList = subjectService.getFiles();
        for(int i=0;i<subjectDtoList.size();i++){
            if(subjectDtoList.get(i).getSubjectName().equals(subjectName)) {
                //기존것을 삭제한다
                System.out.println(subjectDtoList.get(i).getId());

                subjectService.delete(subjectDtoList.get(i).getId());

            }
        }
        //연관관계가 subject > assignment > code순이니까 먼저 subject부터 만들어서 save해준다

        //Make SubjectDto
        SubjectDto subjectDto = SubjectDto.builder().subjectName(subjectName).build();
        Long subjectId = subjectService.saveFile(subjectDto);
        subjectDto.setId(subjectId);
        model.addAttribute("subjects", subjectDto);

        //즉 subject는 제대로 저장되어있다
        System.out.println(subjectRepository.getById(subjectDto.getId()));

        //Make AssignmentDto
        File subject = new File(subjectPath);
        File[] assignmentfile = subject.listFiles();
        for(int k=0;k<assignmentfile.length;k++){
            AssignmentDto assignmentDto = AssignmentDto.builder().assignmentName(assignmentfile[k].getName()).subjectDto(subjectDto)
                    .build();
            Long assignmentId = assignmentService.saveFile(assignmentDto);
            assignmentDto.setId(assignmentId);
            model.addAttribute("assignments", assignmentDto);

            File[] yearfile = assignmentfile[k].listFiles();
            //이제 연도 학번으로 나눠질거임


            //연도수별로 반복을 돈다
            for(int i=0;i< yearfile.length;i++){
                //파일의 자식디렉터리명을 모두 가져옴
                String year = yearfile[i].getName();

                File[] studentNum = yearfile[i].listFiles();
                for(int m=0;m< studentNum.length;m++) {

                    int Idx = studentNum[m].getName().lastIndexOf(".");
                    String deleteExtensionName = studentNum[m].getName().substring(0, Idx);

                    CodeDto code = CodeDto.builder()
                            .code_year(year)
                            .studentNum(deleteExtensionName)
                            .subjectName(subjectName)
                            .assignmentDto(assignmentDto)
                            .filePath(studentNum[m].getPath()).build();
                    System.out.println(assignmentRepository.getById(assignmentDto.getId()));
                    Long codeId = codeService.saveFile(code);
                    code.setId(codeId);

                    model.addAttribute("codes", code);


                }
            }
        }

    }

    public static List<File> decompress(File extractDir, File sourceZipFile) throws Exception {
        InputStream inputStream = null;
        ArchiveInputStream zipInputStream = null;
        List<File> fileList = new ArrayList<File>();

        try {
            inputStream = new FileInputStream(sourceZipFile);
            zipInputStream = new ArchiveStreamFactory().createArchiveInputStream("zip", inputStream);
            ZipArchiveEntry entry = null;
            while ((entry = (ZipArchiveEntry) zipInputStream.getNextEntry()) != null) {
                File entryFile = new File(extractDir, entry.getName());
                if (entry.isDirectory()) {
                    FileUtils.forceMkdir(entryFile);
                } else {
                    FileUtils.forceMkdir(entryFile.getParentFile());
                    OutputStream outputStream = new FileOutputStream(entryFile);
                    try {
                        IOUtils.copy(zipInputStream, outputStream);
                    } finally {
                        outputStream.close();
                    }
                }
                fileList.add(entryFile);
            }
            return fileList;
        } finally {
            if (zipInputStream != null) zipInputStream.close();
            if (inputStream != null) inputStream.close();
        }
    }


}
