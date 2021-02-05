package com.urban.start.service.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.urban.start.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService{

	private String uploadServicePath = "E:/ut/urban-ticket-app/src/assets/image/";
	
	@Override
	public void uploadToLocal(MultipartFile file) {
		// TODO Auto-generated method stub
		try {
			byte[] data = file.getBytes();
			Path path = Paths.get(uploadServicePath + file.getOriginalFilename());
			Files.write(path, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
