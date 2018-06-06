package com.example.library.server.business

import com.example.library.server.dataaccess.Book
import com.example.library.server.dataaccess.BookRepository
import com.example.library.server.dataaccess.User
import com.example.library.server.dataaccess.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.util.IdGenerator
import java.util.*


@Service
@PreAuthorize("hasAnyRole('USER', 'CURATOR', 'ADMIN')")
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val idGenerator: IdGenerator
) {

  @PreAuthorize("hasRole('CURATOR')")
  fun create(bookResource: BookResource) {
    bookRepository.insert(this.convert(bookResource))
  }

  fun findById(uuid: UUID): BookResource? =
    bookRepository.findById(uuid).map(this::convert).orElse(null)

  @PreAuthorize("hasRole('USER')")
  fun borrowById(uuid: UUID?, userId: UUID?) {

    if (uuid == null || userId == null) {
      return
    }

    bookRepository.findById(uuid).ifPresent { book ->
      userRepository.findById(userId).ifPresent { user ->
        book.doBorrow(user)
        bookRepository.save(book)
      }
    }
  }

  @PreAuthorize("hasRole('USER')")
  fun returnById(uuid: UUID?, userId: UUID?) {

    if (uuid == null || userId == null) {
      return;
    }

    bookRepository.findById(uuid).ifPresent { book ->
      userRepository.findById(userId).ifPresent { user ->
        book.doReturn(user)
        bookRepository.save(book)
      }
    }
  }

  fun findAll(): List<BookResource> = bookRepository.findAll().map(this::convert)

  @PreAuthorize("hasRole('CURATOR')")
  fun deleteById(uuid: UUID) {
    bookRepository.deleteById(uuid)
  }

  private fun convert(br: BookResource): Book {
    val optionalUser = br.borrowedBy?.let { borrowedBy ->
      User(borrowedBy.id, borrowedBy.email,
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
      UserResource(borrowedBy.id, borrowedBy.email,
          borrowedBy.firstName, borrowedBy.lastName, borrowedBy.roles)
    }
    return BookResource(b.id, b.isbn, b.title, b.description,
        b.authors, b.borrowed, optionalUser)
  }
}
