package minji.jplag.controller;

import lombok.extern.slf4j.Slf4j;
import minji.jplag.dto.CodeDto;
import minji.jplag.service.CodeService;
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
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class MainController {
    private static CodeService codeService;

    @Autowired//생성자의 인자로 들어오는 변수들에 의존관계를 자동으로 주입해준다
    public MainController(CodeService codeService) throws IOException {
        this.codeService = codeService;
    }
    @GetMapping("/")
    public String defaultFunc(Model model){

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

        return "index";
    }

    public static void makeCodeDto(String subjectPath, String filename, Model model) throws UnsupportedEncodingException {
        File subject = new File(subjectPath);
        File[] fList = subject.listFiles();

        for(int i=0;i<subject.list().length;i++){
            String a = fList[i].toString();
            byte[] euckrStringBuffer  = fList[i].getName().getBytes(Charset.forName("euc-kr"));
            String codeName = new String(euckrStringBuffer, "euc-kr");

            String codePath = subjectPath + "\\" + codeName;
            String[] info = codeName.split("_|\\.");

            CodeDto code = CodeDto.builder()
                    .code_year(info[0])
                    .assignmentNum(info[1])
                    .studentNum(info[2])
                    .subjectName(filename)
                    .studentName(info[3])
                    .filePath(codePath).build();


            codeService.saveFile(code);

            model.addAttribute("codes", code);

            System.out.println(model.getAttribute("codes")+"Sdasdasdsadsadsd");
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
