package my.cousework.barista_helper.api.controllers;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import my.cousework.barista_helper.api.dto.NoteDto;


@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @GetMapping("/{name}")
    public ResponseEntity<NoteDto> getNote(@PathVariable("name") String name) {
        try {
            String content = Files.readString(
                Paths.get(getClass().getResource("/static/notes/" + name + ".md").toURI())
            );
            NoteDto note = new NoteDto(
                name.replace("-", " "),
                content
            );
            return ResponseEntity.ok(note);
        } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found", e);
        }
    }
    
    @GetMapping
    public List<String> getNotesList() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:/static/notes/*.md");
            return Arrays.stream(resources)
                    .map(Resource::getFilename)
                    .map(filename -> filename.replace(".md", ""))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error reading notes", e);
        }
    }
}
