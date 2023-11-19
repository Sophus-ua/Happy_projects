package persistence.dao.services.interfaces;

import models.CustomTagDTO;
import org.springframework.stereotype.Service;

@Service
public interface ICustomTagsService {
    String addCustomTag(CustomTagDTO customTagDTO);
    String deleteCustomTagByIdForUser(CustomTagDTO customTagDTO);

}
