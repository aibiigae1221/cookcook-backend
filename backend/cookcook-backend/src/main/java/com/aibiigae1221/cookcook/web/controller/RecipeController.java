package com.aibiigae1221.cookcook.web.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aibiigae1221.cookcook.data.entity.TemporaryImage;
import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.service.RecipeService;
import com.aibiigae1221.cookcook.service.UserService;
import com.aibiigae1221.cookcook.util.HashMapBean;
import com.aibiigae1221.cookcook.web.domain.AddRecipeParameters;

@RestController
public class RecipeController {

	private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ObjectProvider<HashMapBean> hashMapHolderProvider;
	
	@Value("${spring.servlet.multipart.location}")
	private String uploadLocalPath;
	
	@Value("${user-resource-server-url}")
	private String resourceServerUrl;
	
	@PostMapping("/recipe/upload-image")
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image, Authentication authentication){
		logger.info("유저[{}]님이 파일업로드 중...{}", authentication.getName(), image.getOriginalFilename());

		HashMapBean mapHolder = hashMapHolderProvider.getObject();		
		try {
			TemporaryImage entity = recipeService.saveImagePath(authentication.getName(), image);
			mapHolder.put("status", "success");
			mapHolder.put("imageUrl", entity.getImageUrl());
			return ResponseEntity.status(HttpStatus.OK).body(mapHolder.getSource());
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			mapHolder.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapHolder.getSource());
		}
	}
	
	@PostMapping("/recipe/add-new-recipe")
	public ResponseEntity<?> addNewRecipe(@RequestBody AddRecipeParameters params, Authentication authentication){
		
		logger.info("레시피 만들기 작업중...");
		logger.info(params.toString());
		params.getCookStepList().forEach(cookStep -> logger.info(cookStep.toString()));
		
		User user = userService.loadUserByEmail(authentication.getName());
		recipeService.saveNewRecipe(params, user);
		
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
