package com.example.food_delivery_backend.service;

import com.example.food_delivery_backend.dto.RestaurantRequest;
import com.example.food_delivery_backend.dto.RestaurantResponse;
import com.example.food_delivery_backend.model.Restaurant;
import com.example.food_delivery_backend.Repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repo;

    private RestaurantResponse mapToDTO(Restaurant r) {
        return RestaurantResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .address(r.getAddress())
                .cuisine(r.getCuisine())
                .image(r.getImage())
                .build();
    }

    @Override
    public RestaurantResponse create(RestaurantRequest req) {
        Restaurant r = Restaurant.builder()
                .name(req.getName())
                .address(req.getAddress())
                .cuisine(req.getCuisine())
                .image(req.getImage())
                .build();

        repo.save(r);
        return mapToDTO(r);
    }

    @Override
    public RestaurantResponse update(Long id, RestaurantRequest req) {
        Restaurant r = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        r.setName(req.getName());
        r.setAddress(req.getAddress());
        r.setCuisine(req.getCuisine());
        r.setImage(req.getImage());

        repo.save(r);
        return mapToDTO(r);
    }

//    @Override
//    public void delete(Long id) {
//        if (!repo.existsById(id))
//            throw new RuntimeException("Restaurant not found");
//        repo.deleteById(id);
//    }

    public void deleteRestaurant(Long id) {
        repo.deleteById(id);
    }

    @Override
    public RestaurantResponse getById(Long id) {
        Restaurant r = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        return mapToDTO(r);
    }

    @Override
    public List<RestaurantResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
