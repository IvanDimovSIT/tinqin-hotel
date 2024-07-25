package com.tinqinacademy.hotel.persistence;

import com.tinqinacademy.hotel.persistence.model.Bed;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BedInitializer {
    private final BedRepository bedRepository;

    @PostConstruct
    void init() {
        List<BedSize> bedsInDatabase = bedRepository.findAll().stream()
                .map(Bed::getBedSize)
                .toList();

        List<BedSize> bedsEnums = Arrays.stream(BedSize.values())
                .filter(bedSize -> !bedSize.equals(BedSize.UNKNOWN))
                .toList();

        List<Bed> bedsToAdd = new ArrayList<>();
        for (BedSize bedSize: bedsEnums){
            if (!bedsInDatabase.contains(bedSize)){
                bedsToAdd.add(Bed.builder().bedSize(bedSize).capacity(bedSize.getCapacity()).build());
            }
        }

        bedRepository.saveAll(bedsToAdd);
    }
}
