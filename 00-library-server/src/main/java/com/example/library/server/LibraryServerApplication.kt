package com.example.library.server

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class LibraryServerApplication

fun main(args: Array<String>) {
  SpringApplication.run(LibraryServerApplication::class.java, *args)
}
