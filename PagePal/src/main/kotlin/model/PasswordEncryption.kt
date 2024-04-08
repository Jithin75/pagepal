package model

import org.mindrot.jbcrypt.BCrypt

object PasswordEncryption {
    // Hash a password before storing it
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // Verify a password against its hash
    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}