package com.andaluciaskills.andaluciasckills.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andaluciaskills.andaluciasckills.Service.EvaluacionItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/participantes")
@RequiredArgsConstructor
public class EvaluacionItemController {
    private final EvaluacionItemService evaluacionItemService;
}
