package com.cv_index.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.cv_index.models.CV;

@Repository
public interface CVDaoI extends ElasticsearchRepository<CV, String>{  
    Iterable<CV> findByName(String name);
}
