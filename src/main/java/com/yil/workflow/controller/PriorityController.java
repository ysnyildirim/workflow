package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreatePriorityDto;
import com.yil.workflow.dto.PriorityDto;
import com.yil.workflow.exception.PriorityNotFoundException;
import com.yil.workflow.model.Priority;
import com.yil.workflow.service.PriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/priorities")
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping
    public ResponseEntity<PageDto<PriorityDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Priority> taskPriorityPage = priorityService.findAllByDeletedTimeIsNull(pageable);
        PageDto<PriorityDto> pageDto = PageDto.toDto(taskPriorityPage, PriorityService::toDto);
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<PriorityDto> findById(@PathVariable Long id) throws PriorityNotFoundException {
        Priority priority = priorityService.findByIdAndDeletedTimeIsNull(id);
        PriorityDto dto = PriorityService.toDto(priority);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PriorityDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                              @Valid @RequestBody CreatePriorityDto request) {
        Priority priority = new Priority();
        priority.setName(request.getName());
        priority.setDescription(request.getDescription());
        priority.setCreatedUserId(authenticatedUserId);
        priority.setCreatedTime(new Date());
        priority = priorityService.save(priority);
        PriorityDto dto = PriorityService.toDto(priority);
        return ResponseEntity.created(null).body(dto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PriorityDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @PathVariable Long id,
                                               @Valid @RequestBody CreatePriorityDto request) throws PriorityNotFoundException {
        Priority priority = priorityService.findByIdAndDeletedTimeIsNull(id);
        priority.setName(request.getName());
        priority.setDescription(request.getDescription());
        priority = priorityService.save(priority);
        PriorityDto dto = PriorityService.toDto(priority);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) throws PriorityNotFoundException {
        Priority priority = priorityService.findByIdAndDeletedTimeIsNull(id);
        priority.setDeletedUserId(authenticatedUserId);
        priority.setDeletedTime(new Date());
        priorityService.save(priority);
        return ResponseEntity.ok("TaskPriority deleted.");
    }


}
