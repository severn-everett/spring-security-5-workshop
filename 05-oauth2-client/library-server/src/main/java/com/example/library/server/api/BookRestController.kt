package com.example.library.server.api

import com.example.library.server.business.BookResource
import com.example.library.server.business.BookService
import com.example.library.server.security.LibraryUser
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
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
  fun getAllBooks(): List<BookResource> = bookService.findAll()

  @GetMapping("/books/$PATH_BOOK_ID")
  fun getBookById(@PathVariable(PATH_VARIABLE_BOOK_ID) bookId: UUID): ResponseEntity<BookResource> =
    bookService.findById(bookId)?.let {
      ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(it)
    } ?: notFound().build<BookResource>()

  @PostMapping("/books/$PATH_BOOK_ID/borrow")
  fun borrowBookById(
      @PathVariable(PATH_VARIABLE_BOOK_ID) bookId: UUID, @AuthenticationPrincipal user: LibraryUser): ResponseEntity<Void> {
    bookService.borrowById(bookId, user.userResource.id)
    return ok().build()
  }

  @PostMapping("/books/$PATH_BOOK_ID/return")
  fun returnBookById(
      @PathVariable(PATH_VARIABLE_BOOK_ID) bookId: UUID, @AuthenticationPrincipal user: LibraryUser): ResponseEntity<Void> {
    bookService.returnById(bookId, user.userResource.id)
    return ok().build()
  }

  @PostMapping("/books")
  fun createBook(@Validated @RequestBody bookResource: BookResource): ResponseEntity<Void> {
    bookService.create(bookResource)
    return ok().build()
  }

  @DeleteMapping("/books/$PATH_BOOK_ID")
  fun deleteBook(@PathVariable(PATH_VARIABLE_BOOK_ID) bookId: UUID): ResponseEntity<Void> {
    bookService.deleteById(bookId)
    return ok().build()
  }
}
