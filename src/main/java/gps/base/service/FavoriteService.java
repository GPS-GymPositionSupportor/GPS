package gps.base.service;

import gps.base.DTO.FavoriteDto;
import gps.base.model.Favorite;
import gps.base.model.FavoriteId;
import gps.base.model.Gym;
import gps.base.model.Member;
import gps.base.repository.FavoriteRepository;
import gps.base.repository.GymRepository;
import gps.base.repository.MemberRepository;
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

    public FavoriteDto addFavorite(Long userId, Long gymId) {
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

    public List<FavoriteDto> getUserFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserIdUserId(userId);
        return favorites.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private FavoriteDto convertToDto(Favorite favorite) {
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setUserId(favorite.getUserId().getUserId());
        favoriteDto.setGymId(favorite.getGymId().getGymId());
        favoriteDto.setIsFavorite(favorite.getIsFavorite());
        favoriteDto.setAddedAt(LocalDateTime.now());
        return favoriteDto;
    }
}

