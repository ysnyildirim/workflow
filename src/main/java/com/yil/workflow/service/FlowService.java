package com.yil.workflow.service;

import com.yil.workflow.dto.FlowDto;
import com.yil.workflow.dto.FlowRequest;
import com.yil.workflow.dto.FlowResponce;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.model.Flow;
import com.yil.workflow.repository.FlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class FlowService {

    private final FlowRepository flowRepository;

    public static FlowDto toDto(Flow flow) throws NullPointerException {
        if (flow == null)
            throw new NullPointerException("Flow is null");
        FlowDto dto = new FlowDto();
        dto.setId(flow.getId());
        dto.setDescription(flow.getDescription());
        dto.setEnabled(flow.getEnabled());
        dto.setName(flow.getName());
        return dto;
    }

    public boolean existsById(long id) {
        return flowRepository.existsById(id);
    }

    public Flow findByIdAndDeletedTimeIsNull(Long id) throws FlowNotFoundException {
        return flowRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new FlowNotFoundException());
    }

    public Flow findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id) throws FlowNotFoundException {
        return flowRepository.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(() -> new FlowNotFoundException());
    }

    public FlowResponce save(FlowRequest request, Long userId) {
        Flow flow = new Flow();
        return getFlowResponce(request, userId, flow);
    }

    public FlowResponce replace(FlowRequest request, Long flowId, Long userId) throws FlowNotFoundException {
        Flow flow = findByIdAndDeletedTimeIsNull(flowId);
        return getFlowResponce(request, userId, flow);
    }

    private FlowResponce getFlowResponce(FlowRequest request, Long userId, Flow flow) {
        flow.setName(request.getName());
        flow.setDescription(request.getDescription());
        flow.setEnabled(request.getEnabled());
        flow.setCreatedUserId(userId);
        flow.setCreatedTime(new Date());
        flow = flowRepository.save(flow);
        return FlowResponce.builder().id(flow.getId()).build();
    }

    public void delete(Long id, Long userId) throws FlowNotFoundException {
        Flow flow = findByIdAndDeletedTimeIsNull(id);
        flow.setDeletedUserId(userId);
        flow.setDeletedTime(new Date());
        flowRepository.save(flow);
    }

    public List<Flow> findAllByDeletedTimeIsNull() {
        return flowRepository.findAllByDeletedTimeIsNull();
    }
}
