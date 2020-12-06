package com.cv_index.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cv_index.models.CV;
import com.cv_index.services.CVService;

@RestController
public class CVController {

	@Autowired
	private CVService cvService;

	/**
	 * To add a Cv to elasticsearch
	 * @param mFile
	 * @return all files name from elasticsearch
	 */
	@PostMapping(value = "/CVs/add")
	public List<String> addCv(@RequestParam("file") MultipartFile mFile) {

		try {
			String extension = mFile.getContentType().split("/")[1];
			String content = "";
						
			File file = new File("src/main/resources/targetFile.tmp");
			try (OutputStream os = new FileOutputStream(file)) {
				os.write(mFile.getBytes());
			}

			switch (extension) {
			//Handle pdf files
			case "pdf":				
				content = convertPDF_To_txt(file);
				cvService.save(new CV(content, mFile.getOriginalFilename(), extension));
				
				break;
				
			//Handle docx files
			case "vnd.openxmlformats-officedocument.wordprocessingml.document":
				XWPFDocument docx = new XWPFDocument(new FileInputStream(file));
				XWPFWordExtractor wex = new XWPFWordExtractor(docx);
				cvService.save(new CV(wex.getText(), mFile.getOriginalFilename(), "docx"));
				
				break;

			default:
				break;
			}

			file.delete();

		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterable<CV> iterable = cvService.findAll();
		List<String> result = new ArrayList<String>();
		iterable.forEach(cv -> {
			result.add(cv.getName());
		});

		return result;
	}

	/**
	 * To search for all CVs containing the keyword
	 * @param key : keyword
	 * @return all files name (CVs) containing the keyword(s) from elasticsearch 
	 */
	@GetMapping(value = "/CVs/search/{key}")
	public JSONArray searchByKeyword(@PathVariable String key) {
		List<CV> cvs = cvService.searchByKeywordCV(key);
		JSONArray result = new JSONArray();
		for (CV cv : cvs) {
			result.put(cv.getName());
		}
		return result;
	}

	/**
	 * To get all files by name
	 * @param name
	 * @return list of CVs
	 */
	@GetMapping(value = "/CVs/getByName/{name}")
	public Iterable<CV> findCV(@PathVariable String name) {
		return cvService.findByName(name);
	}

	/**
	 * Get all CVs from elasticsearch
	 * @return list of CVs names
	 */
	@GetMapping(value = "/CVs/getAll")
	public List<String> findAllCVs() {
		Iterable<CV> iterable = cvService.findAll();
		List<String> result = new ArrayList<String>();
		iterable.forEach(cv -> {
			result.add(cv.getName());
		});

		return result;
	}

	/**
	 * To remove CVs files by name
	 * @param name
	 * @return list of files remaining in elasticsearch
	 */
	@DeleteMapping(value = "/CVs/remove/{name}")
	public List<String> deleteCV(@PathVariable String name) {
		Iterable<CV> iterable = cvService.findByName(name);
		iterable.forEach(cv -> {
			cvService.delete(cv);
		});

		List<String> result = new ArrayList<String>();
		iterable = cvService.findAll();
		iterable.forEach(cv -> {
			result.add(cv.getName());
		});

		return result;
	}

	/**
	 * To remove CVs files by id
	 * @param id
	 * @return list of files remaining in elasticsearch
	 */
	@DeleteMapping(value = "/CVs/removeById/{id}")
	public List<String> deleteByIdCV(@PathVariable String id) {
		Optional<CV> cv = cvService.findById(id);
		if (cv.isPresent())
			cvService.delete(cv.get());

		Iterable<CV> iterable = cvService.findAll();
		List<String> result = new ArrayList<String>();
		iterable.forEach(c_v -> {
			result.add(c_v.getName());
		});

		return result;
	}
	
	// -------------------- Fonctions utiles --------------------------

	/**
	 * Convert a file to pdf file and extract text
	 * @param pdf
	 * @return text
	 */
	public String convertPDF_To_txt(File file) {

		String text = "";
		try (PDDocument document = PDDocument.load(file)) {
			document.getClass();
			if (!document.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);
				PDFTextStripper tStripper = new PDFTextStripper();
				text = tStripper.getText(document);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
}
