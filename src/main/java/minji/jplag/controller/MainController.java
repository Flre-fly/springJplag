package minji.jplag.controller;

import lombok.extern.log4j.Log4j2;
import minji.jplag.dto.FileDto;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@Log4j2
public class MainController {

    //RequestMapping이란
    //클라이언트의 요청을 처리할 메서드구현
    @RequestMapping(value="/uploadResult", method= RequestMethod.POST)
    public ResponseEntity<List<FileDto>> fileupload(@RequestParam MultipartFile[] uploadfile, Model model) throws IllegalStateException, IOException {
        List<FileDto> files = new ArrayList<>();
        System.out.println(uploadfile);
        for (MultipartFile file : uploadfile) {
            FileDto dto = new FileDto("201911773", file.getOriginalFilename());
            files.add(dto);
            //test
            System.out.println(file.getOriginalFilename());

            File newFileName = new File(dto.getStudentId() + "_" + dto.getFileName());
            // 전달된 내용을 실제 물리적인 파일로 저장해준다.
            file.transferTo(newFileName);

        }
        model.addAttribute("files", files);//files로 추가했으니 uploadResult.html에거 files로 찾을 수 있어
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
    @PostMapping("/extractZip")
    public static boolean extractZipFiles(String zip_file, String directory) {
        boolean result = false;

        byte[] data = new byte[2048];
        ZipEntry entry = null;
        ZipInputStream zipstream = null;
        FileOutputStream out = null;

        if (!(directory.charAt(directory.length() - 1) == '/'))
            directory += "/";

        File destDir = new File(directory);
        boolean isDirExists = destDir.exists();
        boolean isDirMake = destDir.mkdirs();

        try {
            zipstream = new ZipInputStream(new FileInputStream(zip_file));

            while ((entry = zipstream.getNextEntry()) != null) {

                int read = 0;
                File entryFile;

                //디렉토리의 경우 폴더를 생성한다.
                if (entry.isDirectory()) {
                    File folder = new File(directory+entry.getName());
                    if(!folder.exists()){
                        folder.mkdirs();
                    }
                    continue;
                }else {
                    entryFile = new File(directory + entry.getName());
                }

                if (!entryFile.exists()) {
                    boolean isFileMake = entryFile.createNewFile();
                }

                out = new FileOutputStream(entryFile);
                while ((read = zipstream.read(data, 0, 2048)) != -1)
                    out.write(data, 0, read);

                zipstream.closeEntry();

            }

            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (zipstream != null) {
                try {
                    zipstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


}
