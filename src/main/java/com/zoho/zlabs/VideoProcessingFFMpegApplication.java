package com.zoho.zlabs;

import com.zoho.zlabs.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class VideoProcessingFFMpegApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoProcessingFFMpegApplication.class, args);
	}
}
