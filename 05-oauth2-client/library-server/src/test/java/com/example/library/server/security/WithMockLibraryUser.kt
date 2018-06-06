package com.example.library.server.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.test.context.support.WithSecurityContext

import java.lang.annotation.*

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
@WithSecurityContext(factory = WithMockLibraryUserSecurityContextFactory::class)
annotation class WithMockLibraryUser(
    /**
     * Convenience mechanism for specifying the username. The default is "user". If [ ][.username] is specified it will be used instead of [.value]
     *
     * @return username
     */
    val value: String = "user",
    /**
     * Technical id of the user.
     *
     * @return id
     */
    val id: Long = 1L,
    /**
     * The username to be used. Note that [.value] is a synonym for [.username], but
     * if [.username] is specified it will take precedence.
     *
     * @return username
     */
    val username: String = "user",
    /**
     * The roles to use. The default is "USER". A [GrantedAuthority] will be created for each
     * value within roles. Each value in roles will automatically be prefixed with "ROLE_". For
     * example, the default will result in "ROLE_USER" being used.
     *
     * @return the roles
     */
    val roles: Array<String> = arrayOf("USER"),
    /**
     * The first name to be used. The default is "".
     *
     * @return the first name
     */
    val firstName: String = "",
    /**
     * The last name to be used. The default is "".
     *
     * @return the last name
     */
    val lastName: String = "",
    /**
     * The email to be used. The default is "".
     *
     * @return the email
     */
    val email: String = "",
    /**
     * The UUID to be used.
     *
     * @return the UUID as string
     */
    val identifier: String = "")
