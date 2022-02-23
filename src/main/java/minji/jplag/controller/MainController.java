package minji.jplag.controller;

import lombok.extern.slf4j.Slf4j;
import minji.jplag.Message;
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
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
    @GetMapping("/remove")
    public ModelAndView remove(@RequestParam(value = "subjectName")String[] subjectName, ModelAndView mav){
        //이름같은거 삭제해주기
        List<SubjectDto> subjectDtoList = subjectService.getFiles();
        for(int k=0;k<subjectName.length;k++){
            for (int i=0;i<subjectDtoList.size();i++){
                if(subjectDtoList.get(i).getSubjectName().equals(subjectName[k])){
                    subjectService.delete(subjectDtoList.get(i).getId());
                    //String fileDir = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/files").toString();
                    String fileDir = "C:\\Users";

                    //서버에 저장되어있는 파일도 삭제해준다
                    deleteFolder(fileDir + "/" + subjectName[k]);
                }
            }
        }



        //경고창뜨게
        mav.addObject("data", new Message("삭제가 완료되었습니다.", "/"));
        mav.setViewName("message");
        return mav;

    }
    @GetMapping("/")
    public String mainpage(Model model){
        List<SubjectDto> subjectDtoList = subjectService.getFiles();
        List<AssignmentDto> assignmentDtoList = assignmentService.getFiles();
        List<CodeDto> codeDtoList = codeService.getFiles();
        model.addAttribute("subjects", subjectDtoList);
        model.addAttribute("assignments", assignmentDtoList);
        model.addAttribute("codes", codeDtoList);
        return "main";
    }

    //RequestMapping이란
    //클라이언트의 요청을 처리할 메서드구현
    @PostMapping("/upload")
    public ModelAndView fileupload(@RequestParam MultipartFile uploadfile,
                             Model model, ModelAndView mav) throws Throwable {

        //그냥 버튼만 누르는 경우 zip파일 안만들고 그냥 return하기
        if(uploadfile.isEmpty()) {
            mav.addObject("data", new Message("빈 파일을 업로드할 수 없습니다.", "/"));
            mav.setViewName("message");

            return mav;
        }

        //userdir와 뒤에있는 path를 결합하여 하나의 path로 만드는과정인거임
        //String fileDir = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/files").toString();
        String fileDir = "C:\\Users";
        //이거 이름같을경우 예외처리도 해줘야함
        //확장자 제거
        int Idx = uploadfile.getOriginalFilename().lastIndexOf(".");
        String filename = uploadfile.getOriginalFilename().substring(0, Idx);
        String subjectPath = fileDir + "\\" + filename;


        List<SubjectDto> subjects=subjectService.getFiles();
        for(int i=0;i<subjects.size();i++){
            //똑같은걸 넣어줬으면 이전꺼 삭제해야지
           if(subjects.get(i).getSubjectName().equals(filename)){
               deleteFolder(subjectPath);
           }
        }


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
        model.addAttribute("results","");

        //디렉터리 구조바꾸기 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
        changeStructure(subjectPath);

        //경고창뜨게
        mav.addObject("data", new Message("업로드가 완료되었습니다.", "/"));
        mav.setViewName("message");

        return mav;
    }
    //과제
    //ㄴ2018
    //  ㄴ학번.java

    //과제
    //ㄴ2018_학번.java 이런식으로 저장해야함
    public static void changeStructure(String path) throws IOException {
        //과목에 해당하는 path가 인자로 들어온다고 가정
        File file = Paths.get(path).toFile();


        String[] assignmentList = file.list();
        //이파일에다가 다집어넣고 이름을나중에 바꿀거다.

        String allCode = "all";
        for(int i=0;i<assignmentList.length;i++){

            File tempDir = new File(path +"/" + assignmentList[i]+ "/" + allCode);
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }


            File assignmentDir = Paths.get(path + "/" + assignmentList[i]).toFile();
            String[] yearList = assignmentDir.list();
            for(int k=0;k< yearList.length;k++){
                if(yearList[k].equals(allCode)){
                    continue;
                }
                String[] codeList = Paths.get(path + "/" + assignmentList[i] + "/" + yearList[k]).toFile().list();
                for(int j=0;j<codeList.length;j++){
                    copy(path + "/" + assignmentList[i] + "/"+yearList[k] +  "/" + codeList[j], path
                            + "/" + assignmentList[i] + "/" + allCode + "/" +yearList[k] +  "_" + codeList[j]);

                }
                deleteFolder(path + "/" + assignmentList[i] + "/"+yearList[k]);
            }
            //원래있던과제 삭제
            //디렉터리 삭제



        }
    }
    public static void copy(String source, String target) throws IOException {

        // 1. 원본 File, 복사할 File 준비
        File file = new File(source);
        File newFile = new File(target);
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
    public static void deleteFolder(String path) {

        File folder = new File(path);
        try {
            if(folder.exists()){
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기

                for (int i = 0; i < folder_list.length; i++) {
                    if(folder_list[i].isFile()) {
                        folder_list[i].delete();
                        System.out.println("파일이 삭제되었습니다.");
                    }else {
                        deleteFolder(folder_list[i].getPath()); //재귀함수호출
                        System.out.println("폴더가 삭제되었습니다.");
                    }
                    folder_list[i].delete();
                }
                folder.delete(); //폴더 삭제
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
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
                if(studentNum == null){

                    int Idx = year.lastIndexOf(".");
                    String deleteExtensionName = year.substring(0, Idx);

                    //null이면
                    //과제
                    //ㄴ연도_학번이 들어왔따고 가정한다
                    CodeDto code = CodeDto.builder()
                            .code_year(year)
                            .studentNum(deleteExtensionName)
                            .subjectName(subjectName)
                            .assignmentDto(assignmentDto)
                            .filePath(yearfile[i].getPath()).build();
                    System.out.println(assignmentRepository.getById(assignmentDto.getId()));
                    Long codeId = codeService.saveFile(code);
                    code.setId(codeId);

                    model.addAttribute("codes", code);


                }
                else{
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

    }
    public static void dirCopy(File sourceF, File targetF){
        File[] target_file = sourceF.listFiles();
        for (File file : target_file) {
            File temp = new File(targetF.getAbsolutePath() + File.separator + file.getName());
            if(file.isDirectory()){
                temp.mkdir();
                dirCopy(file, temp);
            } else {
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(temp) ;
                    byte[] b = new byte[4096];
                    int cnt = 0;
                    while((cnt=fis.read(b)) != -1){
                        fos.write(b, 0, cnt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally{
                    try {
                        fis.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
