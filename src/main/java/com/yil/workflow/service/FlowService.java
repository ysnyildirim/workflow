package com.yil.workflow.service;

import com.yil.workflow.dto.FlowDto;
import com.yil.workflow.dto.FlowRequest;
import com.yil.workflow.dto.FlowResponse;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.model.Flow;
import com.yil.workflow.repository.FlowDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FlowService {

    private final FlowDao flowDao;

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
        return flowDao.existsById(id);
    }

    public Flow findByIdAndDeletedTimeIsNull(Long id) throws FlowNotFoundException {
        return flowDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new FlowNotFoundException());
    }

    public Flow findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id) throws FlowNotFoundException {
        return flowDao.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(() -> new FlowNotFoundException());
    }

    public boolean existsByIdAndEnabledTrueAndDeletedTimeIsNull(Long id) {
        return flowDao.existsByIdAndEnabledTrueAndDeletedTimeIsNull(id);
    }

    public List<Flow> getStartUpFlows(long userId) {
        return flowDao.getStartUpFlows(userId);
    }

    @Transactional
    public FlowResponse save(FlowRequest request, Long userId) {
        Flow flow = new Flow();
        return getFlowResponce(request, userId, flow);
    }

    @Transactional
    public FlowResponse replace(FlowRequest request, Long flowId, Long userId) throws FlowNotFoundException {
        Flow flow = findByIdAndDeletedTimeIsNull(flowId);
        return getFlowResponce(request, userId, flow);
    }

    private FlowResponse getFlowResponce(FlowRequest request, Long userId, Flow flow) {
        flow.setName(request.getName());
        flow.setDescription(request.getDescription());
        flow.setEnabled(request.getEnabled());
        flow.setCreatedUserId(userId);
        flow.setCreatedTime(new Date());
        flow = flowDao.save(flow);
        return FlowResponse.builder().id(flow.getId()).build();
    }

    @Transactional
    public void delete(Long id, Long userId) throws FlowNotFoundException {
        Flow flow = findByIdAndDeletedTimeIsNull(id);
        flow.setDeletedUserId(userId);
        flow.setDeletedTime(new Date());
        flowDao.save(flow);
    }

    public List<Flow> findAllByDeletedTimeIsNull() {
        return flowDao.findAllByDeletedTimeIsNull();
    }
}
