package com.example.library.server.api

import com.example.library.server.business.BookResource
import com.example.library.server.business.BookService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

/**
 * REST api for books.
 */
@RestController
class BookRestController(private val bookService: BookService) {

  companion object {
    private const val PATH_VARIABLE_BOOK_ID = "bookId"
    private const val PATH_BOOK_ID = "{$PATH_VARIABLE_BOOK_ID}"
  }

  @GetMapping("/books")
  fun getAllBooks(): Flux<BookResource> = bookService.findAll()

  @GetMapping("/books/$PATH_BOOK_ID")
  fun getBookById(@PathVariable(PATH_VARIABLE_BOOK_ID) bookId: UUID): Mono<ResponseEntity<BookResource>> =
    bookService.findById(bookId)
      .map{ ResponseEntity.ok(it) }
      .defaultIfEmpty(ResponseEntity.notFound().build())

  @PostMapping("/books/$PATH_BOOK_ID/borrow")
  fun borrowBookById(@PathVariable(PATH_VARIABLE_BOOK_ID) bookId: UUID): Mono<Void> =
    bookService.borrowById(bookId, null)

  @PostMapping("/books/$PATH_BOOK_ID/return")
  fun returnBookById(@PathVariable(PATH_VARIABLE_BOOK_ID) bookId: UUID): Mono<Void> =
    bookService.returnById(bookId, null)

  @PostMapping("/books")
  fun createBook(@Validated @RequestBody bookResource: Mono<BookResource>): Mono<Void> =
    bookService.create(bookResource)

  @DeleteMapping("/books/$PATH_BOOK_ID")
  fun deleteBook(@PathVariable(PATH_VARIABLE_BOOK_ID) bookId: UUID): Mono<Void> =
    bookService.deleteById(bookId)
}
