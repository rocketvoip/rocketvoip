/*
 * Copyright 2017 Jona Braun, Benedikt Herzog, Rafael Ostertag,
 *                Marcel Schöni, Marco Studerus, Martin Wittwer
 *
 * Redistribution and  use in  source and binary  forms, with  or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions  of  source code  must retain  the above  copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce  the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation   and/or   other    materials   provided   with   the
 *    distribution.
 *
 * THIS SOFTWARE  IS PROVIDED BY  THE COPYRIGHT HOLDERS  AND CONTRIBUTORS
 * "AS  IS" AND  ANY EXPRESS  OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE  ARE DISCLAIMED. IN NO EVENT  SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL DAMAGES  (INCLUDING,  BUT  NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF  LIABILITY, WHETHER IN  CONTRACT, STRICT LIABILITY,  OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN  ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.zhaw.psit4.services.implementation.adapters;

import ch.zhaw.psit4.data.jpa.entities.Branch;
import ch.zhaw.psit4.data.jpa.entities.BranchDialPlan;
import ch.zhaw.psit4.data.jpa.entities.DialPlan;
import ch.zhaw.psit4.data.jpa.repositories.BranchDialPlanRepository;
import ch.zhaw.psit4.data.jpa.repositories.BranchRepository;
import ch.zhaw.psit4.data.jpa.repositories.DialPlanRepository;
import ch.zhaw.psit4.dto.ActionDto;
import ch.zhaw.psit4.dto.DialPlanDto;
import ch.zhaw.psit4.dto.actions.BranchActionDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static ch.zhaw.psit4.services.implementation.DialPlanServiceImpl.dialPlanDtoToDialPlanEntityWithId;

/**
 * Helps to handle Branch entities.
 *
 * @author Jona Braun
 */
public class BranchAdapter implements ActionAdapterInterface {

    public static final String TYPE = "Branch";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final BranchRepository branchRepository;
    private final DialPlanRepository dialPlanRepository;
    private final BranchDialPlanRepository branchDialPlanRepository;

    public BranchAdapter(BranchRepository branchRepository, DialPlanRepository dialPlanRepository, BranchDialPlanRepository branchDialPlanRepository) {
        this.branchRepository = branchRepository;
        this.dialPlanRepository = dialPlanRepository;
        this.branchDialPlanRepository = branchDialPlanRepository;
    }

    private static BranchActionDto branchEntityToBranchActionDto(Branch branch) {
        BranchActionDto branchActionDto = new BranchActionDto();
        branchActionDto.setHangupTime(branch.getHangupTime());
        List<Long> dialPlanIds = branchDialPlansToDialPlanIds(branch.getBranchesDialPlans());
        branchActionDto.setNextDialPlanIds(dialPlanIds);
        return branchActionDto;
    }

    private static List<Long> branchDialPlansToDialPlanIds(List<BranchDialPlan> branchDialPlanList) {
        // create a map sorted by the buttonNumber
        TreeMap<Integer, Long> branchDialPlanTreeMap = new TreeMap<>();
        branchDialPlanList.forEach(x ->
                branchDialPlanTreeMap.put(x.getButtonNumber(), x.getDialPlan().getId()));

        List<Long> dialPlanIds = new ArrayList<>();
        dialPlanIds.addAll(branchDialPlanTreeMap.values());

        return dialPlanIds;
    }

    private ActionDto branchEntityToActionDto(Branch branch) {
        ActionDto actionDto = new ActionDto();
        actionDto.setId(branch.getId());
        actionDto.setName(branch.getName());
        actionDto.setType(TYPE);
        BranchActionDto branchActionDto = branchEntityToBranchActionDto(branch);
        Map<String, Object> map = OBJECT_MAPPER.convertValue(branchActionDto, Map.class);
        actionDto.setTypeSpecific(map);
        return actionDto;
    }


    @Override
    public void saveActionDto(DialPlanDto dialPlanDto, ActionDto actionDto, int priority) {
        if (TYPE.equalsIgnoreCase(actionDto.getType())) {
            BranchActionDto branchActionDto = OBJECT_MAPPER.convertValue(actionDto.getTypeSpecific(), BranchActionDto.class);

            List<DialPlan> nextDialPlans = new ArrayList<>();

            branchActionDto.getNextDialPlanIds().forEach(x ->
                    nextDialPlans.add(dialPlanRepository.findFirstById(x)));

            List<BranchDialPlan> branchDialPlans = saveBranchDialPlans(nextDialPlans);

            Branch branchEntity = new Branch(
                    actionDto.getName(),
                    priority,
                    dialPlanDtoToDialPlanEntityWithId(dialPlanDto),
                    branchDialPlans,
                    branchActionDto.getHangupTime());

            branchRepository.save(branchEntity);

        }

    }

    @Override
    public ActionDto retrieveActionDto(long dialPlanId, int priority) {
        Branch branchEntity = branchRepository.findFirstByDialPlanIdAndPriority(dialPlanId, priority);
        if (branchEntity != null) {
            return branchEntityToActionDto(branchEntity);
        }
        return null;
    }

    @Override
    public void deleteActionDto(long dialPlanId) {
        branchRepository.deleteAllByDialPlanId(dialPlanId);
        List<Branch> branchEntity = branchRepository.findAllByDialPlanId(dialPlanId);
        branchEntity.forEach(x -> branchDialPlanRepository.delete(x.getBranchesDialPlans()));
    }

    private List<BranchDialPlan> saveBranchDialPlans(List<DialPlan> nextDialPlans) {
        List<BranchDialPlan> branchDialPlans = new ArrayList<>();
        int buttonNumber = 0;
        for (DialPlan dialPlan : nextDialPlans) {
            buttonNumber++;
            BranchDialPlan branchDialPlan = branchDialPlanRepository.save(new BranchDialPlan(dialPlan, buttonNumber));
            branchDialPlans.add(branchDialPlan);
        }
        return branchDialPlans;
    }
}
