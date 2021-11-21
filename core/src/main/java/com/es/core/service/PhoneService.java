package com.es.core.service;

import com.es.core.model.phone.Phone;
import com.es.core.model.search.SearchStructure;

import java.util.List;
import java.util.Optional;

public interface PhoneService {
    Optional<Phone> getPhone(Long id);
    List<Phone> findPhones(SearchStructure search, int offset, int limit);
    void save(Phone phone);
    long count(String query);
}
