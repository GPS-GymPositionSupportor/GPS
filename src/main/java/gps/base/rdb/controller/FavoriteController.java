package gps.base.rdb.controller;

import gps.base.rdb.DTO.FavoriteDTO;
import gps.base.rdb.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private FavoriteService favoriteService;




    /*
    즐겨 찾기 관련
     */

    // 즐겨찾기 추가
    @PostMapping("/favorite/add")
    public ResponseEntity<FavoriteDTO> addFavorite(@RequestParam Long userId, @RequestParam Long gymId) {
        FavoriteDTO favoriteDto = favoriteService.addFavorite(userId, gymId);
        return ResponseEntity.ok(favoriteDto);
    }

    // 즐겨찾기 삭제
    @PostMapping("/favorite/remove")
    public ResponseEntity<Void> removeFavoriteUsingPost(@RequestParam Long userId, @RequestParam Long gymId) {
        favoriteService.removeFavorite(userId, gymId);
        return ResponseEntity.noContent().build();
    }

    // 특정 유저의 즐겨찾기 조회
    @GetMapping("/favorite/user/{userId}")
    public String getUserFavorites(@PathVariable Long userId, Model model) {
        List<FavoriteDTO> favorites = favoriteService.getUserFavorites(userId);
        model.addAttribute("favoriteList", favorites);
        return "favoriteTest";
    }
}
