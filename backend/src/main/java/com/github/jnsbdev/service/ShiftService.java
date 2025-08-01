package com.github.jnsbdev.service;

import com.github.jnsbdev.dto.shift.ShiftCreateDTO;
import com.github.jnsbdev.dto.shift.ShiftDTO;
import com.github.jnsbdev.entity.Shift;
import com.github.jnsbdev.mapper.ShiftMapper;
import com.github.jnsbdev.repository.ShiftRepository;
import com.github.jnsbdev.validation.ShiftValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final IdService idService;
    private final ShiftValidator shiftValidator;

    public ShiftDTO saveShift(ShiftCreateDTO shiftCreateDTO) {
        UUID id = idService.randomId();
        Shift shiftToSave = ShiftMapper.toShift(shiftCreateDTO, id);
        shiftValidator.validateShift(shiftToSave);
        Shift saved = shiftRepository.save(shiftToSave);
        return ShiftMapper.toShiftDto(saved);
    }

    public List<ShiftDTO> getShifts() {
        return shiftRepository.findAll()
                .stream()
                .map(ShiftMapper::toShiftDto)
                .toList();
    }

    public Optional<ShiftDTO> getShiftById(UUID id) {
        return shiftRepository.findById(id).map(ShiftMapper::toShiftDto);
    }

    public ShiftDTO updateShift(UUID id, ShiftDTO shiftToUpdate) {
        if (!shiftRepository.existsById(id)) {
            throw new NoSuchElementException(String.format("shift not found with the id %s", id));
        }
        if (!id.equals(shiftToUpdate.id())) {
            throw new IllegalArgumentException("ID is not changeable");
        }
        Shift shift = ShiftMapper.toShift(shiftToUpdate);
        shiftValidator.validateShift(shift);
        Shift saved = shiftRepository.save(shift);
        return ShiftMapper.toShiftDto(saved);
    }

    public boolean deleteShiftById(UUID id) {
        if (shiftRepository.existsById(id)) {
            shiftRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
