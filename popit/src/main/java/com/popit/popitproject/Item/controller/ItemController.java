package com.popit.popitproject.Item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.Item.repository.ItemRepository;
import com.popit.popitproject.Item.service.ItemService;
import com.popit.popitproject.Item.service.ItemService.ItemNotFoundException;
import com.popit.popitproject.Item.service.S3Service;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.user.service.JwtTokenService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller")
public class ItemController {

  private final ItemService itemService;
  private final JwtTokenService jwtTokenService;

  @PostMapping(path = "/profile/item/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> register(
      @RequestPart("itemInput") String itemInputStr,
      @RequestPart("file") MultipartFile file,
      HttpServletRequest request) throws IOException {

    String token = request.getHeader("Authorization").substring(7); // Extract token
    if (!jwtTokenService.validateToken(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    }

    String userId = jwtTokenService.getSellerIdFromToken(token);

    ObjectMapper objectMapper = new ObjectMapper();
    ItemInput itemInput = objectMapper.readValue(itemInputStr, ItemInput.class);
    itemInput.setFile(file);


    Item item = itemService.registerItem(itemInput, userId);

    return new ResponseEntity<>(item, HttpStatus.CREATED);
  }

  @PatchMapping(path = "/item/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Item> updateItemImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) throws IOException {
    Item updatedItem = itemService.updateItemImage(id, file);
    return new ResponseEntity<>(updatedItem, HttpStatus.OK);
  }

  @PatchMapping("/profile/item/update/{id}")
  public ResponseEntity<Item> update(@PathVariable Long id, @RequestBody ItemInput itemInput) {
    Item item = itemService.updateItem(id, itemInput);
    return new ResponseEntity<>(item, HttpStatus.OK);
  }

  @DeleteMapping("/profile/item/delete/{id}")
  public ResponseEntity<String> delete(@PathVariable Long id) {
    try {
      itemService.deleteItem(id);
      return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
    } catch (ItemNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/item/{userId}")
  public List<Item> getItem(@PathVariable String userId) {
    return itemService.getItemsByUserId(userId);
  }


}

