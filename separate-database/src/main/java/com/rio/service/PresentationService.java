package com.rio.service;

import com.rio.entity.Presentation;
import com.rio.repository.PresentationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PresentationService {

  private final PresentationRepository presentationRepository;

  public List<Presentation> getAll() {
    return presentationRepository.findAll();
  }

  public Presentation save(Presentation presentation) {
    return presentationRepository.save(presentation);
  }
}
