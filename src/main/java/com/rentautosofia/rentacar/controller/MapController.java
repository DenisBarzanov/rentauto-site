/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.rentautosofia.rentacar.controller.MapController
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 */
package com.rentautosofia.rentacar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {
    @GetMapping(value={"/taking_place"})
    public String index(Model model) {
        model.addAttribute("view", "car/map");
        return "base-layout";
    }
}

