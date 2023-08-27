
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    @SerialName("access_token")
    val accessToken: String, // rfx2uswqe8l4g1mkagrvg5tv0ks3
    @SerialName("expires_in")
    val expiresIn: Int, // 14124
    @SerialName("refresh_token")
    val refreshToken: String, // 5b93chm6hdve3mycz05zfzatkfdenfspp1h1ar2xxdalen01
    @SerialName("scope")
    val scope: List<String>,
    @SerialName("token_type")
    val tokenType: String // bearer
)