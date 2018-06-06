package com.example.library.server.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException


@Component
class LibraryUserAuthenticationConverter(
    private val userDetailsService: UserDetailsService
) : UserAuthenticationConverter {

  companion object {
    private const val USER_NAME_ENTRY = "user_name"
    private const val CLIENT_ID_ENTRY = "client_id"
  }

  private val logger = LoggerFactory.getLogger(LibraryUserAuthenticationConverter::class.java)

  override fun convertUserAuthentication(authentication: Authentication): Map<String, *> =
    LinkedHashMap<String, Any>().apply {
      put(UserAuthenticationConverter::USERNAME.toString(), authentication.name)
      if (authentication.authorities.isNotEmpty()) {
        put(
            UserAuthenticationConverter::AUTHORITIES.toString(),
            AuthorityUtils.authorityListToSet(authentication.authorities)
        )
      }
    }

  override fun extractAuthentication(map: Map<String, *>): Authentication {

    logger.debug("Contents of JWT token $map")

    // Check if token had expected client id
    val clientValue = map[CLIENT_ID_ENTRY] as String

    if (!clientValue.equals("library-client", ignoreCase = true)) {
      logger.warn("Invalid client id {} detected", clientValue)
      throw BadClientCredentialsException()
    }

    map[USER_NAME_ENTRY]?.let {
      val principal = getUserDetails(it as String, map)
      return UsernamePasswordAuthenticationToken(
          principal, "n/a", (principal as LibraryUser).authorities)
    } ?: throw BadClientCredentialsException()
  }

  private fun getUserDetails(userId: String, map: Map<String, *>?): UserDetails {
    try {
      return userDetailsService.loadUserByUsername(userId)
    } catch (ex: UsernameNotFoundException) {
      // do not rethrow original exception (not reveal details to user)
      map?.let {
        logger.info("Could not load user. Register as new user.", ex)
      } ?: logger.warn("Could not load user", ex)
      throw BadClientCredentialsException()
    }
  }

}
