package com.example.healthcare_api.controllers;

import com.example.healthcare_api.dtos.EmailDTO;
import com.example.healthcare_api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/send_email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping()
    public String sendEmail(@RequestBody EmailDTO emailDTO) {
        emailService.sendHtmlMessage(emailDTO);
        return "Email sent successfully";
    }
}
