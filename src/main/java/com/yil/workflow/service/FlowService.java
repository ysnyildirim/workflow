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

    public static FlowDto convert(Flow flow) {
        FlowDto dto = new FlowDto();
        dto.setId(flow.getId());
        dto.setDescription(flow.getDescription());
        dto.setEnabled(flow.isEnabled());
        dto.setName(flow.getName());
        return dto;
    }

    @Transactional(readOnly = true)
    public boolean existsByIdAndEnabledTrueAndDeletedTimeIsNull(Long id) {
        return flowDao.existsByIdAndEnabledTrueAndDeletedTimeIsNull(id);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public FlowResponse save(FlowRequest request, Long userId) {
        Flow flow = new Flow();
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

    @Transactional(rollbackFor = {Throwable.class})
    public FlowResponse replace(FlowRequest request, Long flowId, Long userId) throws FlowNotFoundException {
        Flow flow = flowDao.findByIdAndDeletedTimeIsNull(flowId).orElseThrow(FlowNotFoundException::new);
        return getFlowResponce(request, userId, flow);
    }

    @Transactional(readOnly = true)
    public Flow findByIdAndDeletedTimeIsNull(Long id) throws FlowNotFoundException {
        return flowDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(FlowNotFoundException::new);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void delete(Long id, Long userId) throws FlowNotFoundException {
        Flow flow = flowDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(FlowNotFoundException::new);
        flow.setDeletedUserId(userId);
        flow.setDeletedTime(new Date());
        flowDao.save(flow);
    }

    @Transactional(readOnly = true)
    public List<Flow> findAllByDeletedTimeIsNull() {
        return flowDao.findAllByDeletedTimeIsNull();
    }

    @Transactional(readOnly = true)
    public List<Flow> findAllByDeletedTimeIsNullAndEnabledTrue() {
        return flowDao.findAllByDeletedTimeIsNullAndEnabledTrue();
    }


}
