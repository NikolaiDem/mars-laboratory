package ru.ase.mars.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class MarsController {

    @GetMapping
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok("Just do it");
    }

}
