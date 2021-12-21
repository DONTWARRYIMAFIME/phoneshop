package com.es.core.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.search.SearchStructure;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PhoneDao {
    Optional<Phone> get(Long key);
    Optional<Phone> getByModel(final String model);
    void save(Phone phone);
    List<Phone> findAll(SearchStructure searchStructure, int offset, int limit);
    void updatePhoneColors(Long phoneId, Set<Color> colors);
    long count(String query);
}
