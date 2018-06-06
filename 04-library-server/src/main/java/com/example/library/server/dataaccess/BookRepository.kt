package com.example.library.server.dataaccess

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import java.util.*

interface BookRepository: ReactiveMongoRepository<Book, UUID>
