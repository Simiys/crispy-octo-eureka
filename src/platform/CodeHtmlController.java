package platform;

import org.apache.commons.collections4.IterableUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.Duration;

@Controller
public class CodeHtmlController {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public CodeSnippetRepository repository;


    @GetMapping("/code/{uuid}")
        public String ApiCode (Model model, @PathVariable String uuid) {
        CodeSnippet codeSnippet = repository.findByuuid(uuid);

        try {
            String code = codeSnippet.getCode();
        } catch (NullPointerException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Snippet not found"// if current snippet is absent return 404 error
            );
        }

        LocalDateTime creationDate = LocalDateTime.parse(codeSnippet.getDate(),formatter);
        Duration duration = Duration.between(creationDate,LocalDateTime.now());
        long timePassed = duration.toSeconds();//time passed from uploading current snippet

        switch (codeSnippet.getRestrictionType()) {
            case UNRESTRICTED:
                System.out.println("/code/id  unrestricted");
                model.addAttribute("date",codeSnippet.getDate());
                model.addAttribute("code",codeSnippet.getCode());
                return "GetCode";

            case VIEW_RESTRICTED:
                System.out.println("/code/id  view restricted");
                if(codeSnippet.getAllowedViews() > 0) {
                    codeSnippet.minusViews();
                    repository.save(codeSnippet);
                    model.addAttribute("date",codeSnippet.getDate());
                    model.addAttribute("code",codeSnippet.getCode());
                    model.addAttribute("views",codeSnippet.getAllowedViews());
                    return "GetCodeViewRestricted";
                }
            case TIME_RESTRICTED:
                System.out.println("/code/id  time restricted");
                if (codeSnippet.getTime() > timePassed) {// if difference between allowed time and passed time below zero we dont return snippet, and trow 404 not found exeption
                    repository.save(codeSnippet);
                    model.addAttribute("date",codeSnippet.getDate());
                    model.addAttribute("code",codeSnippet.getCode());
                    model.addAttribute("views",0);
                    model.addAttribute("time",codeSnippet.getTime() - timePassed);
                    return "GetCodeTimeRestricted";
                }
            case RESTRICTED:
                System.out.println("/code/id  restricted");
                if (codeSnippet.getAllowedViews() > 0 && codeSnippet.getTime() > timePassed) {
                    codeSnippet.minusViews();
                    repository.save(codeSnippet);
                    model.addAttribute("date",codeSnippet.getDate());
                    model.addAttribute("code",codeSnippet.getCode());
                    model.addAttribute("views",codeSnippet.getAllowedViews());
                    model.addAttribute("time",codeSnippet.getTime() - timePassed);
                    return "GetCodeRestricted";
                }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Snippet not found"
        );
    }

    @GetMapping("/code/new")
    public String CodeNew (Model model) {
        return "CodeNew";
    }

    @GetMapping("/code/latest")
    public String LatestCode (Model model) {
        System.out.println("/code/latest");
        List<CodeSnippet> latest = new ArrayList<>();//this ils list of 10 or less latest and not restricted snippets of code
        Iterable<CodeSnippet> iterable = repository.findAll();
        List<CodeSnippet> all = new ArrayList<>(IterableUtils.toList(iterable));//this is list of all code snippets in database
        //List<CodeSnippet> reversedAll = new ArrayList<>();

        int size = IterableUtils.size(iterable) - 1;
        int a = 10;
        for (int i = 0;i < a;i++) {
            if(i > size) {
                break;
            }
            if (all.get(size - i).getRestrictionType().equals(RestrictionType.UNRESTRICTED)){
                latest.add(all.get(size - i));//we should not show restricted snippets at latest
            } else {
                a++;
            }

        }
        model.addAttribute("snpts",latest);
        return "Latest";
    }



}

@RestController
class RestControl {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public CodeSnippetRepository repository;

    @GetMapping("/api/code/{uuid}")
    public SnippetForLatestAPI GetApiCode(@PathVariable String uuid) {
        CodeSnippet codeSnippet = repository.findByuuid(uuid);
        try {
            String code = codeSnippet.getCode();
        } catch (NullPointerException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Snippet not found"// if current snippet is absent return 404 error
            );
        }
        SnippetForLatestAPI snippetWithoutId;
        LocalDateTime creationDate = LocalDateTime.parse(codeSnippet.getDate(),formatter);
        Duration duration = Duration.between(creationDate,LocalDateTime.now());
        long timePassed = duration.toSeconds();


        switch (codeSnippet.getRestrictionType()) {
            case UNRESTRICTED:
                System.out.println("/api/code/id  unrestricted");
                 snippetWithoutId = new SnippetForLatestAPI(codeSnippet);
                return snippetWithoutId;
            case VIEW_RESTRICTED:
                System.out.println("/api/code/id  view restricted");
                if(codeSnippet.getAllowedViews() > 0) {
                    codeSnippet.minusViews();
                    repository.save(codeSnippet);
                    snippetWithoutId = new SnippetForLatestAPI(codeSnippet);
                    return snippetWithoutId;
                }
            case TIME_RESTRICTED:
                System.out.println("/api/code/id  time restricted");
                if (codeSnippet.getTime() > timePassed) {
                    repository.save(codeSnippet);
                    snippetWithoutId = new SnippetForLatestAPI(codeSnippet);
                    snippetWithoutId.setTime(codeSnippet.getTime() - timePassed);
                    return snippetWithoutId;
                }
            case RESTRICTED:
                System.out.println("/api/code/id  restricted");
                if (codeSnippet.getAllowedViews() > 0 && codeSnippet.getTime() > timePassed) {
                    codeSnippet.minusViews();
                    repository.save(codeSnippet);
                    snippetWithoutId = new SnippetForLatestAPI(codeSnippet);
                    snippetWithoutId.setTime(codeSnippet.getTime() - timePassed);
                    return snippetWithoutId;
                }
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Snippet not found"
        );
    }

    @PostMapping("/api/code/new")
    public Map<String, String> ApiCodeNew(@RequestBody CodeBearer code) {
        System.out.println("/api/code/new");

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        CodeSnippet c = new CodeSnippet(code.getCode(), code.getViews(), code.getTime());
        c.setUuid(id);
        repository.save(c);
        System.out.println(id);
        return Map.of("id", String.valueOf(id));
    }

    @GetMapping("/api/code/latest")
    public List<SnippetForLatestAPI> latestCodeApi () {
        System.out.println("/api/code/latest");
        List<SnippetForLatestAPI> latest = new ArrayList<>();
        Iterable<CodeSnippet> iterable = repository.findAll();
        List<CodeSnippet> all = new ArrayList<>(IterableUtils.toList(iterable));

        int size = IterableUtils.size(iterable) - 1;
        int a = 10;
        for (int i = 0;i < a;i++) {
            if(i > size) {
                break;
            }
            if (all.get(size - i).getRestrictionType().equals(RestrictionType.UNRESTRICTED)) {
                latest.add(new SnippetForLatestAPI(all.get(size - i)));
            } else {
                a++;
            }
        }
        return latest;
    }

}

//this class is needed to take a JSON from CodeNew
class CodeBearer {
    String code;

    int views;

    int time;

    public int getViews() {
        return views;
    }

    public String getCode() {
        return code;
    }

    public int getTime() {
        return time;
    }
}





