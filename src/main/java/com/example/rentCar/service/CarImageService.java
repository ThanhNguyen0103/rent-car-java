package com.example.rentCar.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.rentCar.domain.Car;
import com.example.rentCar.domain.CarImage;
import com.example.rentCar.repository.CarImageRepository;

@Service
public class CarImageService {
    @Value("${thanh.upload-file.base-path}")
    private String basePath;
    private final CarImageRepository carImageRepository;
    private final CarService carService;

    public CarImageService(CarImageRepository carImageRepository, CarService carService) {
        this.carImageRepository = carImageRepository;
        this.carService = carService;
    }

    public CarImage uploadCarImage(long carId, String folder, MultipartFile file)
            throws URISyntaxException, IOException {
        URI folderUri = new URI(this.basePath + folder);
        Path folderPath = Paths.get(folderUri);
        File dir = new File(folderPath.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(this.basePath + folder + "/" + fileName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        CarImage carImage = new CarImage();
        Car car = this.carService.getCarById(carId);

        carImage.setUrl(fileName);
        carImage.setCar(car);

        return this.carImageRepository.save(carImage);
    }

}
