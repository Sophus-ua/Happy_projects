package persistence.dao.services.implementations;

import lombok.extern.slf4j.Slf4j;
import models.CustomTagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import persistence.dao.repositories.ICustomTagRepository;
import persistence.dao.services.interfaces.ICustomTagsService;
import persistence.entity.CustomTag;


@Slf4j
@Service
public class CustomTagsServiceImpl implements ICustomTagsService {
    private ICustomTagRepository customTagRepository;

    @Override
    public String addCustomTag(CustomTagDTO customTagDTO) {
        if (customTagDTO == null || customTagDTO.getName() == null || customTagDTO.getName().isEmpty())
            return ResultOfAction.ADD_FAILED.message;

        CustomTag customTag = new CustomTag();
        customTag.setName(customTagDTO.getName());
        customTagRepository.save(customTag);

        return ResultOfAction.ADD_SUCCESSFUL.message;
    }

    @Override
    public String deleteCustomTagById(CustomTagDTO customTagDTO) {
        if (customTagDTO == null || customTagDTO.getId() == 0)
            return ResultOfAction.DELETE_FAILED.message;

        customTagRepository.deleteById(customTagDTO.getId());
        customTagRepository.resetAutoIncrement();

        if (customTagRepository.existsById(customTagDTO.getId()))
            return ResultOfAction.DELETE_FAILED.message;
        else
            return ResultOfAction.DELETE_SUCCESSFUL.message;
    }


    enum ResultOfAction {
        ADD_SUCCESSFUL("ТЕГ успішно додано"),
        DELETE_SUCCESSFUL("ТЕГ успішно видалено"),
        ADD_FAILED("Невдалось зберегти ТЕГ :("),
        DELETE_FAILED("Невдалось видалити ТЕГ :(");
        private final String message;

        ResultOfAction(String message) {
            this.message = message;
        }

    }

    @Autowired
    public void setCustomTagRepository(ICustomTagRepository customTagRepository) {
        this.customTagRepository = customTagRepository;
    }
}
