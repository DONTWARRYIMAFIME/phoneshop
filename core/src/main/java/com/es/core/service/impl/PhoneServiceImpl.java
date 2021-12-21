package com.es.core.service.impl;

import com.es.core.dao.ColorDao;
import com.es.core.dao.PhoneDao;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.model.search.SearchStructure;
import com.es.core.service.PhoneService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

@Service
public class PhoneServiceImpl implements PhoneService {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private ColorDao colorDao;

    @Override
    public Phone getPhoneById(Long id) {
        return phoneDao.get(id).orElseThrow(() -> new PhoneNotFoundException(id));
    }

    @Override
    public Phone getPhoneByModel(String model) throws PhoneNotFoundException {
        return phoneDao.getByModel(model).orElseThrow(() -> new PhoneNotFoundException("Phone with MODEL: " + model + " not found"));
    }

    @Override
    public List<Phone> findPhones(SearchStructure search, int offset, int limit) {
        String query = search.getQuery();

        if (query != null && !query.isEmpty()) {
            search.setQuery(query.trim().toLowerCase(Locale.ROOT));
        }

        return phoneDao.findAll(search, offset, limit);
    }

    @Override
    public void save(Phone phone) {
        phoneDao.save(phone);
        colorDao.saveAll(phone.getColors());
        phoneDao.updatePhoneColors(phone.getId(), phone.getColors());
    }

    @Override
    public long count(String query) {
        if (query != null) {
            query = query.trim().toLowerCase();
        }

        return phoneDao.count(query);
    }
}
