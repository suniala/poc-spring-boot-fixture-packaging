package com.example.fixturepackaging

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class MyController {
    init {
        println("INIT!")
    }

    @GetMapping
    fun getHep(): String = "hep!"
}