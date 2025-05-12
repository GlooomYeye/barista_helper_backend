package my.cousework.barista_helper.api.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.store.entities.TermEntity;
import my.cousework.barista_helper.store.repository.TermRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TermService {
    TermRepository termRepository;
    
    @Transactional(readOnly = true)
    public List<TermEntity> getAll(){
        return termRepository.findAll();
    };
}
