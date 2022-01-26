package minji.jplag.controller;

import minji.jplag.dto.FileDto;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {
    @GetMapping("/")
    public String defaultFunc(){

    }

    //RequestMapping이란
    //클라이언트의 요청을 처리할 메서드구현
    @RequestMapping(value="/upload", method= RequestMethod.GET)
    public String fileupload(@RequestParam MultipartFile[] uploadfile, Model model) throws IllegalStateException, IOException {
        List<FileDto> list = new ArrayList<>();
        for (MultipartFile file : uploadfile) {
            if (!file.isEmpty()) {
                // UUID를 이용해 unique한 파일 이름을 만들어준다.
                FileDto dto = new FileDto("201911773", file.getName());
                list.add(dto);

                File newFileName = new File(dto.getStudentId() + "_" + dto.getFileName());
                // 전달된 내용을 실제 물리적인 파일로 저장해준다.
                file.transferTo(newFileName);
            }
        }
        model.addAttribute("files", list);//files로 추가했으니 uploadResult.html에거 files로 찾을 수 있어
        return "/uploadResult";
    }

}
