package com.cv_index.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cv_index.models.CV;

@Service
public interface CVServiceI {

	List<CV> searchByKeywordCV(String keyword);
}
