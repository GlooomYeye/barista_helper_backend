package my.cousework.barista_helper.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import my.cousework.barista_helper.store.entities.StepEntity;

@Service
public class StepService {
    public List<StepEntity> getAllByRecipeId(Long recipeId) {
        return null;
    };

    public StepEntity create(StepEntity step, Long recipeId) {
        return null;
    };

    public StepEntity update(StepEntity step) {
        return null;
    };

    public Void delete(Long id) {
        return null;
    };
}
