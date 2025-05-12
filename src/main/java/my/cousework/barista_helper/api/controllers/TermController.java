package my.cousework.barista_helper.api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.dto.TermDto;
import my.cousework.barista_helper.api.mappers.TermMapper;
import my.cousework.barista_helper.api.services.TermService;
import my.cousework.barista_helper.store.entities.TermEntity;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/terms")
public class TermController {
    TermService termService;
    TermMapper termMapper;
    @GetMapping("")
    public List<TermDto> getAll() {
        List<TermEntity> terms = termService.getAll();
        return termMapper.toDto(terms);
    }
}
