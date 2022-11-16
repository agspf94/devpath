package com.devpath.controller

import com.devpath.dto.job.request.CreateJobRequest
import com.devpath.dto.job.response.DeleteJobResponse
import com.devpath.dto.job.request.UpdateJobRequest
import com.devpath.entity.Job
import com.devpath.service.JobService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/job")
class JobController(
    private val jobService: JobService
){
    @PostMapping("/create")
    fun create(@RequestBody createJobRequest: CreateJobRequest): Job {
        return jobService.create(createJobRequest)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): Job {
        return jobService.read(id)
    }

    @GetMapping("/all")
    fun readAll(): List<Job> {
        return jobService.readAll()
    }

    @PatchMapping("/update")
    fun update(@RequestBody updateJobRequest: UpdateJobRequest): Job {
        return jobService.update(updateJobRequest)
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: Int): DeleteJobResponse {
        return jobService.delete(id)
    }
}
