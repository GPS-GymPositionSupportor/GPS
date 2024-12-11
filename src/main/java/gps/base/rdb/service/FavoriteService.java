package gps.base.rdb.service;

import gps.base.rdb.DTO.FavoriteDTO;
import gps.base.rdb.model.Favorite;
import gps.base.rdb.model.FavoriteId;
import gps.base.rdb.model.Gym;
import gps.base.rdb.model.Member;
import gps.base.rdb.repository.FavoriteRepository;
import gps.base.rdb.repository.GymRepository;
import gps.base.rdb.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private MemberRepository memberRepository;

    public FavoriteDTO addFavorite(Long userId, Long gymId) {
        Optional<Member> member = memberRepository.findById(userId);
        Optional<Gym> gym = gymRepository.findById(gymId);

        if (member.isPresent() && gym.isPresent()) {
            Favorite favorite = new Favorite(member.get(), gym.get(), true);
            Favorite savedFavorite = favoriteRepository.save(favorite);
            return convertToDto(savedFavorite);
        } else {
            throw new RuntimeException("User or Gym not found");
        }
    }

    public void removeFavorite(Long userId, Long gymId) {
        favoriteRepository.deleteById(new FavoriteId(userId, gymId));
    }

    public List<FavoriteDTO> getUserFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private FavoriteDTO convertToDto(Favorite favorite) {
        FavoriteDTO favoriteDto = new FavoriteDTO();
        favoriteDto.setUserId(favorite.getUser().getUserId());
        favoriteDto.setGymId(favorite.getGym().getGymId());
        favoriteDto.setIsFavorite(favorite.getIsFavorite());
        favoriteDto.setAddedAt(LocalDateTime.now());
        return favoriteDto;
    }
}
