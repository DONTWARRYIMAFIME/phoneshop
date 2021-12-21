package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.service.PhoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/products")
public class ProductDetailsPageController {
    @Resource
    private PhoneService phoneService;

    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        try {
            Phone phone = phoneService.getPhoneById(id);
            model.addAttribute("phone", phone);
        } catch (PhoneNotFoundException e) {
            return "error/404";
        }

        return "product";
    }
}

