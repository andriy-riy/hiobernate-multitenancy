package com.rio.controller;

import com.rio.entity.Presentation;
import com.rio.service.PresentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/presentations")
@RequiredArgsConstructor
public class PresentationController {

  private final PresentationService presentationService;

  @GetMapping
  public List<Presentation> getAll() {
    return presentationService.getAll();
  }

  @PostMapping
  public Presentation create(@RequestBody Presentation presentation) {
    return presentationService.save(presentation);
  }
}
