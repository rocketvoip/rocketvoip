package ch.zhaw.psit4.services.implementation;

import ch.zhaw.psit4.data.jpa.repositories.DialRepository;
import ch.zhaw.psit4.data.jpa.repositories.SayAlphaRepository;
import ch.zhaw.psit4.data.jpa.repositories.SipClientRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.services.interfaces.ActionServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the ActionServiceInterface.
 *
 * @author Jona Braun
 */
@Service
public class ActionServiceImpl implements ActionServiceInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionServiceImpl.class);
    private final SipClientRepository sipClientRepository;
    private final DialRepository dialRepository;
    private final SayAlphaRepository sayAlphaRepository;

    public ActionServiceImpl(SipClientRepository sipClientRepository,
                             DialRepository dialRepository,
                             SayAlphaRepository sayAlphaRepository) {
        this.sipClientRepository = sipClientRepository;
        this.dialRepository = dialRepository;
        this.sayAlphaRepository = sayAlphaRepository;
    }

    /**
     * @inheritDocs
     */
    @Override
    public void saveActions(DialPlanDto dialPlanDto) {

    }

    /**
     * @inheritDocs
     */
    @Override
    public void updateActions(DialPlanDto dialPlanDto) {

    }

    /**
     * @inheritDocs
     */
    @Override
    public List<ActionDto> retrieveActions(long dialPlanId) {
        return null;
    }

    /**
     * @inheritDocs
     */
    @Override
    public void deleteActions(long dialPlanId) {

    }


}
