package com.cv_index.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cv_index.dao.CVDaoI;
import com.cv_index.models.CV;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Component
public class CVService implements CVServiceI{
	
	private String host;
	private int port;
	RestHighLevelClient client;
	private CVDaoI cvDao;
	
    public CVService(@Value("${elasticsearch.host}") String host, 
    		@Value("${elasticsearch.port}") Integer port) {
        this.host = host;
        this.port = port;
        this.client = new RestHighLevelClient( RestClient.builder(new HttpHost(this.host, this.port, "http")));
    }
	
    @Autowired
    public void setRepository(CVDaoI cvDao) {
        this.cvDao = cvDao;
    }
    
	@Autowired
	private ObjectMapper objMapper;
	
			
	public CV save(CV cv) {
		return cvDao.save(cv);
	}

	public void delete(CV cv) {
		cvDao.delete(cv);
	}

	public Optional<CV> findById(String id) {
		return cvDao.findById(id);
	}

	public Iterable<CV> findAll() {
		return cvDao.findAll();
	}

	public Iterable<CV> findByName(String name) {
		return cvDao.findByName(name);
	}

	@Override
	public List<CV> searchByKeywordCV(String content) {
		
		SearchRequest searchR = new SearchRequest();
		searchR.indices("cv_s");
		SearchSourceBuilder searchSB = new SearchSourceBuilder();
		
		searchSB.query(QueryBuilders.queryStringQuery(content).field("content"));
		searchR.source(searchSB);
		SearchResponse searchRes = null;
		
		List<CV> cvs = new ArrayList<CV>();
		
		try {
			searchRes = client.search(searchR, RequestOptions.DEFAULT);
			if(searchRes.getHits().getTotalHits().value > 0){
				SearchHit[] searchHits = searchRes.getHits().getHits();
				for(SearchHit hit : searchHits) {
					Map<String, Object> map = hit.getSourceAsMap();
					cvs.add(objMapper.convertValue(map, CV.class));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return cvs;
	}

}
