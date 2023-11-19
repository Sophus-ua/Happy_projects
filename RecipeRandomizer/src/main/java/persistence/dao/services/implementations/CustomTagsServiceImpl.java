package persistence.dao.services.implementations;

import lombok.extern.slf4j.Slf4j;
import models.CustomTagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import persistence.dao.repositories.ICustomTagRepository;
import persistence.dao.repositories.IUserRepository;
import persistence.dao.services.interfaces.ICustomTagsService;
import persistence.entity.CustomTag;
import persistence.entity.User;

import java.util.Optional;


@Slf4j
@Service
public class CustomTagsServiceImpl implements ICustomTagsService {
    private ICustomTagRepository customTagRepository;
    private IUserRepository userRepository;

    @Override
    public String addCustomTag(CustomTagDTO customTagDTO) {
        if (customTagDTO == null || customTagDTO.getName() == null || customTagDTO.getName().isEmpty() ||
                customTagDTO.getUsername() == null || customTagDTO.getUsername().isEmpty())
            return ResultOfAction.ADD_FAILED.message;

        CustomTag customTag = new CustomTag();
        customTag.setName(customTagDTO.getName());

        Optional<User> optional = userRepository.findByUsername(customTagDTO.getUsername());
        if (!optional.isPresent())
            return ResultOfAction.ADD_FAILED.message;

        customTag.setUser(optional.get());

        customTagRepository.save(customTag);
        return ResultOfAction.ADD_SUCCESSFUL.message;
    }

    @Override
    public String deleteCustomTagByIdForUser(CustomTagDTO customTagDTO) {
        if (customTagDTO == null || customTagDTO.getId() == 0 ||
                customTagDTO.getUsername() == null || customTagDTO.getUsername().isEmpty())
            return ResultOfAction.DELETE_FAILED.message;

        customTagRepository.deleteByIdAndUsername(customTagDTO.getId(), customTagDTO.getUsername());
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
    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
