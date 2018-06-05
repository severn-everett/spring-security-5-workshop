package com.example.library.server.business

import com.example.library.server.dataaccess.Book
import com.example.library.server.dataaccess.BookRepository
import com.example.library.server.dataaccess.User
import com.example.library.server.dataaccess.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.util.IdGenerator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
@PreAuthorize("hasAnyRole('USER', 'CURATOR', 'ADMIN')")
open class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val idGenerator: IdGenerator
) {

  @PreAuthorize("hasRole('CURATOR')")
  open fun create(bookResource: Mono<BookResource>): Mono<Void> =
    bookRepository.insert(bookResource.map(this::convert)).then()

  open fun findById(uuid: UUID): Mono<BookResource> =
    bookRepository.findById(uuid).map(this::convert)

  @PreAuthorize("hasRole('USER')")
  open fun borrowById(uuid: UUID?, userId: UUID?): Mono<Void> {
    if ((uuid == null) || (userId == null)) {
      return Mono.empty()
    }

    return bookRepository
      .findById(uuid)
      .log()
      .flatMap { b ->
        userRepository
          .findById(userId)
          .flatMap { u ->
            b.doBorrow(u)
            bookRepository.save(b).then()
          }.switchIfEmpty(Mono.empty())
      }.switchIfEmpty(Mono.empty())
  }

  @PreAuthorize("hasRole('USER')")
  open fun returnById(uuid: UUID?, userId: UUID?): Mono<Void> {
    if ((uuid == null) || (userId == null)) {
      return Mono.empty()
    }

    return bookRepository
      .findById(uuid)
      .log()
      .flatMap { b ->
        userRepository
          .findById(userId)
          .flatMap { u ->
            b.doReturn(u)
            bookRepository.save(b).then()
          }.switchIfEmpty(Mono.empty())
      }.switchIfEmpty(Mono.empty())
  }

  open fun findAll(): Flux<BookResource> =
    bookRepository.findAll().map(this::convert)

  @PreAuthorize("hasRole('CURATOR')")
  open fun deleteById(uuid: UUID): Mono<Void> =
    bookRepository.deleteById(uuid).then()

  private fun convert(br: BookResource): Book {
    val optionalUser = br.borrowedBy?.let { borrowedBy ->
      User(borrowedBy.id, borrowedBy.email, borrowedBy.password,
          borrowedBy.firstName, borrowedBy.lastName, borrowedBy.roles)
    }
    return Book(
        idGenerator.generateId(), br.isbn, br.title, br.description,
        br.authors, br.borrowed,
        optionalUser
    )
  }

  private fun convert(b: Book): BookResource {
    val optionalUser = b.borrowedBy?.let { borrowedBy ->
      UserResource(borrowedBy.id, borrowedBy.email, borrowedBy.password,
          borrowedBy.firstName, borrowedBy.lastName, borrowedBy.roles)
    }
    return BookResource(b.id, b.isbn, b.title, b.description,
        b.authors, b.borrowed, optionalUser)
  }
}
