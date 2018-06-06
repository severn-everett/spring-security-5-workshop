package com.example.oauth2loginclient.api

import java.util.*

class UserInfo {
  var id: UUID? = null
  var email: String? = null
  var firstname: String? = null
  var lastname: String? = null
  var roles: List<String>? = null
}
