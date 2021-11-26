package com.es.core.dao;

import com.es.core.model.phone.Color;

import java.util.Optional;
import java.util.Set;

public interface ColorDao {
    Optional<Color> get(Long key);
    void saveAll(Set<Color> colors);
}
