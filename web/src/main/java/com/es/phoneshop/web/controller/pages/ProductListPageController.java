package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.core.model.search.SearchStructure;
import com.es.core.model.search.SortField;
import com.es.core.model.search.SortOrder;
import com.es.core.service.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping (value = "/productList")
public class ProductListPageController {
    
    private static final int PHONES_PER_PAGE = 10;

    @Resource
    private PhoneService phoneService;

    @GetMapping
    public String showProductList(@RequestParam(name = "page", defaultValue = "1") int page,
                                  @RequestParam(name = "query", required = false) String query,
                                  @RequestParam(name = "sortBy", required = false) String sortBy,
                                  @RequestParam(name = "orderBy", required = false) String orderBy,
                                  Model model) {
        SearchStructure search = new SearchStructure(query, SortField.safeValueOf(sortBy), SortOrder.safeValueOf(orderBy));

        int offset = (page - 1) * PHONES_PER_PAGE;
        List<Phone> phoneList = phoneService.findPhones(search, offset, PHONES_PER_PAGE);

        long phoneQuantity = phoneService.count(query);

        long numOfPages = phoneQuantity / PHONES_PER_PAGE;
        numOfPages = phoneQuantity % PHONES_PER_PAGE != 0 ? numOfPages + 1 : numOfPages;

        model.addAttribute("page", page);
        model.addAttribute("pages", numOfPages);
        model.addAttribute("phones", phoneList);
        model.addAttribute("phoneQuantity", phoneQuantity);
        return "productList";
    }
}
