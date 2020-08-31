package com.oyku.spring.web;

import com.oyku.spring.domain.Project;
import com.oyku.spring.domain.ProjectTask;
import com.oyku.spring.repositories.BacklogRepository;
import com.oyku.spring.repositories.ProjectTaskRepository;
import com.oyku.spring.services.MapValidationErrorService;
import com.oyku.spring.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private BacklogRepository backlogRepository;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addProjectTaskBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlog_id, Principal principal){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id,projectTask,principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }
    
    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectTasks(@PathVariable String backlog_id, Principal principal){

        return projectTaskService.findBacklogById(backlog_id,principal.getName());
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id,Principal principal){

        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id,pt_id,principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String backlog_id, @PathVariable String pt_id,Principal principal){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask,backlog_id,pt_id,principal.getName());
        return new ResponseEntity<ProjectTask>(updatedTask,HttpStatus.OK);
    }

    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id,Principal principal){
        projectTaskService.deletePTByProjectSequence(backlog_id,pt_id,principal.getName());

        return new ResponseEntity<String>("Project task with '"+pt_id+"'successfully deleted.",HttpStatus.OK);
    }
}
