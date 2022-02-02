package minji.jplag.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import minji.jplag.dto.FileDto;
import minji.jplag.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import net.lingala.zip4j.ZipFile;
import java.util.zip.ZipInputStream;

@Controller
@Slf4j

public class MainController {
    //@Value("${file.dir}")
    //private String fileDir;



    private FileService fileService;

    public MainController(FileService fileService) throws IOException {
        this.fileService = fileService;
    }
    @GetMapping("upload")
    public String upload(){
        return "uploadResult";
    }
    //RequestMapping이란
    //클라이언트의 요청을 처리할 메서드구현
    @PostMapping("/upload")
    public String fileupload(@RequestParam MultipartFile uploadfile,
                             @RequestParam String fileName,//
                             Model model,
                             HttpServletRequest request) throws IllegalStateException, IOException {

        //String fileDir = new ClassPathResource("/static/files").getFile().getAbsolutePath();

        //userdir와 뒤에있는 path를 결합하여 하나의 path로 만드는과정인거임
        String fileDir = Paths.get(System.getProperty("user.dir"), "src/main/resources/static/files").toString();

        String fullPath = fileDir +"\\"+ fileName;// /아니고 \\임
        String filePath = fileDir +"\\"+ fileName + ".zip";
        //이거 이름같을경우 예외처리도 해줘야함
        FileDto file = FileDto.builder()
                .origFilename(uploadfile.getOriginalFilename())
                .filename(uploadfile.getName())
                .filePath(fullPath)
                .build();
        //압축파일 넘어온것까진 괜찮은데.. 이제..압축파일을 풀어야함


        //String despath = new ClassPathResource(fullPath).getFile().getAbsolutePath();
        //압축파일이 multipartfile의 경로..
        String beforeFilePath = fileDir +"\\"+"before.zip";
        File beforeZipFile = new File(beforeFilePath);
        uploadfile.transferTo(new File(beforeFilePath));

        extractZipFiles(beforeZipFile,fileDir);

        beforeZipFile.delete();

        //uploadfile.transferTo(new File(fullPath));//fileDir에 파일 저장, 파일에 대한 경로는 fullPath
        fileService.saveFile(file);

        model.addAttribute("file", file);//files로 추가했으니 uploadResult.html에거 files로 찾을 수 있어


        return "uploadResult";
    }

    public static void extractZipFiles(File source, String targetDir) throws IOException {

        ZipFile zipFile = new ZipFile(source);
        zipFile.extractAll(targetDir);

    }


}
