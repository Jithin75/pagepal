
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.runBlocking
import model.DatabaseManager
import model.PasswordEncryption.verifyPassword
import model.UserModel
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.junit.Assert
import org.junit.Test
import viewmodel.ProfilePageViewModel
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProfilePageTest {
    val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
        MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
    )

    val client = MongoClient.create(connectionString = "mongodb+srv://praviin10:Prav2003@cluster0.fqt7qpj.mongodb.net/?retryWrites=true&w=majority")
    val database = client.getDatabase("PagePalDB").withCodecRegistry(pojoCodecRegistry);
    val dbManager = DatabaseManager(database)

    @Test
    fun deleteAccount() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        runBlocking {
            dbManager.addUser(testUser)
            profile.deleteAccount()
            var retrievedUser = dbManager.getUserByUsername("testUser")
            Assert.assertNull(retrievedUser)
            dbManager.deleteUser("testuser")
        }
    }

    @Test
    fun updateUsername(){
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        runBlocking {
            dbManager.addUser(testUser)
            profile.toggleNewUsername("test")
            profile.updateUsername()
            var retrievedUser = dbManager.getUserByUsername("testUser")
            Assert.assertNull(retrievedUser)
            retrievedUser = dbManager.getUserByUsername("test")
            Assert.assertEquals(retrievedUser?.username, testUser.username)
            Assert.assertEquals(testUser.username, "test")
            dbManager.deleteUser("test")
            dbManager.deleteUser("testuser")
        }
    }

    @Test
    fun updatePassword() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        runBlocking {
            dbManager.addUser(testUser)
            profile.toggleNewPassword("test")
            profile.toggleCurrentPassword("testuser")
            profile.updatePassword()
            val updatedUser = dbManager.getUserByUsername("testuser")
            val valid = updatedUser?.let { dbManager.isValidCredentials(it.username, "test") }
            val notValid = updatedUser?.let { dbManager.isValidCredentials(it.username, "testuser") }
            if (valid != null) {
                Assert.assertTrue(valid)
            }
            if (notValid != null) {
                Assert.assertFalse(notValid)
            }
            Assert.assertTrue(verifyPassword("test", testUser.password))
            Assert.assertFalse(verifyPassword("testuser", testUser.password))
            dbManager.deleteUser("testuser")
        }
    }

    @Test
    fun toggleShowPasswordDialog() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        profile.toggleShowPasswordDialog(true)
        assertTrue(profile.showPasswordDialog)
        profile.toggleShowPasswordDialog(false)
        assertFalse(profile.showPasswordDialog)
    }
    @Test
    fun toggleShowUsernameChangeDialog() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        profile.toggleShowUsernameChangeDialog(true)
        assertTrue(profile.showUsernameChangeDialog)
        profile.toggleShowUsernameChangeDialog(false)
        assertFalse(profile.showUsernameChangeDialog)
    }
    @Test
    fun toggleShowConfirmationDialog() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        profile.toggleShowConfirmationDialog(true)
        assertTrue(profile.showConfirmationDialog)
        profile.toggleShowConfirmationDialog(false)
        assertFalse(profile.showConfirmationDialog)
    }
    @Test
    fun toggleCurrentPassword() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        profile.toggleCurrentPassword("new")
        assertEquals(profile.currentPassword, "new")
   }
    @Test
    fun toggleNewPassword()  {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        profile.toggleNewPassword("new")
        assertEquals(profile.newPassword, "new")
    }
    @Test
    fun toggleVerifyPassword() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        profile.toggleVerifyPassword("new")
        assertEquals(profile.verifyPassword, "new")
    }
    @Test
    fun toggleNewUsername() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        profile.toggleNewUsername("new")
        assertEquals(profile.newUsername, "new")
    }
    @Test
    fun toggleVerifyUsername() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        profile.toggleVerifyUsername("new")
        assertEquals(profile.verifyUsername, "new")
    }
    @Test
    fun toggleErrorMessage() {
        val testUser = UserModel(username = "testuser", password = "testuser")
        var profile = ProfilePageViewModel(testUser, dbManager)
        profile.toggleErrorMessage("Error")
        assertEquals(profile.errorMessage, "Error")
    }
}