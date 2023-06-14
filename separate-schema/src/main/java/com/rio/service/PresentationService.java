package com.rio.service;

import com.rio.entity.Presentation;
import com.rio.repository.PresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresentationService {

  private final PresentationRepository presentationRepository;

  public List<Presentation> getAll() {
    return presentationRepository.findAll();
  }
}
