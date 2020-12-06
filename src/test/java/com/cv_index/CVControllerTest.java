package com.cv_index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cv_index.controllers.CVController;
import com.cv_index.dao.CVDaoI;
import com.cv_index.models.CV;
import com.cv_index.services.CVService;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CVController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = CVsIndexElasticsearchApplication.class)
public class CVControllerTest {
	
	CV cv = new CV("This is a test", "test1.pdf", "pdf");
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private CVService cvService;
	
	@MockBean
	private CVDaoI cvDao;
	
	@Test
	void saveCv() {
		cvService.save(cv);
		assertNotNull(cvService.findById(cv.getId()));		
	}
	
	@Test
	void searchCv() {
		List<CV> cvs = cvService.searchByKeywordCV("This");
		cvs.forEach(e -> assertEquals(e.getName(), cv.getName()));
	}

	@Test
	void getCvByName() {
		Iterable<CV> cvs = cvService.findByName("test1.pdf");
		cvs.forEach(e -> assertEquals(e.getName(), cv.getName()));
	}

	@Test
	void deleteCv() {
		cvService.delete(cv);
		Iterable<CV> cvs = cvService.findAll();
		cvs.forEach(e -> assertNotEquals(e.getId(), cv.getId()));
	}

}
