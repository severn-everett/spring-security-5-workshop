package com.example.oauth2loginclient.api

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

class UserResource {
  var id: UUID? = null
  var email: String? = null
  var firstName: String? = null
  var lastName: String? = null
  @JsonIgnore
  var roles: List<String>? = null
}
