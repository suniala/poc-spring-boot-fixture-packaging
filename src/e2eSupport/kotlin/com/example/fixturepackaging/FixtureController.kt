package com.example.fixturepackaging

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fixtures")
class FixtureController(private val testData: TestData) {
    init {
        println("FIXTURE CONTROLLER")
    }

    @GetMapping(path = ["f1"])
    fun fixture1(): String {
        val someTestData: ProdDataType = testData.someTestData()
        return someTestData.field1
    }
}