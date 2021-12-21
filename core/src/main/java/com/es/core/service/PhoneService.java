package com.es.core.service;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.model.search.SearchStructure;

import java.util.List;

public interface PhoneService {
    Phone getPhoneById(Long id) throws PhoneNotFoundException;
    Phone getPhoneByModel(String model) throws PhoneNotFoundException;
    List<Phone> findPhones(SearchStructure search, int offset, int limit);
    void save(Phone phone);
    long count(String query);
}
