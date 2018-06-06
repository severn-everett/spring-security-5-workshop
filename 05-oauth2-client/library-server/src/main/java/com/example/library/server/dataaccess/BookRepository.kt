package com.example.library.server.dataaccess

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface BookRepository: MongoRepository<Book, UUID>
