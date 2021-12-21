package com.es.core.service.impl;

import com.es.core.dao.ColorDao;
import com.es.core.dao.PhoneDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.search.SearchStructure;
import com.es.core.model.search.SortField;
import com.es.core.model.search.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PhoneServiceImplTest {
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private ColorDao colorDao;
    @Mock
    private Phone phone;

    @InjectMocks
    private PhoneServiceImpl phoneService;

    @Before
    public void init() {
        when(phoneDao.get(100L)).thenReturn(Optional.of(phone));
    }

    @Test
    public void testGetPhone() {
        phoneService.getPhoneById(100L);

        verify(phoneDao, times(1)).get(100L);
    }

    @Test
    public void testSavePhone() {
        phoneService.save(phone);

        verify(phoneDao).save(phone);
        verify(colorDao).saveAll(phone.getColors());
        verify(phoneDao).updatePhoneColors(phone.getId(), phone.getColors());
    }

    @Test
    public void testGetPhoneList() {
        SearchStructure searchStructure = new SearchStructure(null, SortField.BRAND, SortOrder.ASC);
        phoneService.findPhones(searchStructure, 0 , 10);

        verify(phoneDao).findAll(searchStructure, 0, 10);
    }

    @Test
    public void testGetPhonesCount() {
        phoneService.count(null);
        verify(phoneDao).count(null);
    }
}
