begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|com.google.gerrit.server.auth.oauth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|auth
operator|.
name|oauth
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|extensions
operator|.
name|proto
operator|.
name|ProtoTruth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|testing
operator|.
name|SerializedClassSubject
operator|.
name|assertThatSerializedClass
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|auth
operator|.
name|oauth
operator|.
name|OAuthToken
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|proto
operator|.
name|Cache
operator|.
name|OAuthTokenProto
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|serialize
operator|.
name|CacheSerializer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|JUnit4
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
DECL|class|OAuthTokenCacheTest
specifier|public
specifier|final
class|class
name|OAuthTokenCacheTest
block|{
annotation|@
name|Test
DECL|method|oAuthTokenSerializer ()
specifier|public
name|void
name|oAuthTokenSerializer
parameter_list|()
throws|throws
name|Exception
block|{
name|OAuthToken
name|token
init|=
operator|new
name|OAuthToken
argument_list|(
literal|"token"
argument_list|,
literal|"secret"
argument_list|,
literal|"raw"
argument_list|,
literal|12345L
argument_list|,
literal|"provider"
argument_list|)
decl_stmt|;
name|CacheSerializer
argument_list|<
name|OAuthToken
argument_list|>
name|s
init|=
operator|new
name|OAuthTokenCache
operator|.
name|Serializer
argument_list|()
decl_stmt|;
name|byte
index|[]
name|serialized
init|=
name|s
operator|.
name|serialize
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|OAuthTokenProto
operator|.
name|parseFrom
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|OAuthTokenProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setToken
argument_list|(
literal|"token"
argument_list|)
operator|.
name|setSecret
argument_list|(
literal|"secret"
argument_list|)
operator|.
name|setRaw
argument_list|(
literal|"raw"
argument_list|)
operator|.
name|setExpiresAt
argument_list|(
literal|12345L
argument_list|)
operator|.
name|setProviderId
argument_list|(
literal|"provider"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|s
operator|.
name|deserialize
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|oAuthTokenSerializerWithNullProvider ()
specifier|public
name|void
name|oAuthTokenSerializerWithNullProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|OAuthToken
name|tokenWithNull
init|=
operator|new
name|OAuthToken
argument_list|(
literal|"token"
argument_list|,
literal|"secret"
argument_list|,
literal|"raw"
argument_list|,
literal|12345L
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|CacheSerializer
argument_list|<
name|OAuthToken
argument_list|>
name|s
init|=
operator|new
name|OAuthTokenCache
operator|.
name|Serializer
argument_list|()
decl_stmt|;
name|OAuthTokenProto
name|expectedProto
init|=
name|OAuthTokenProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setToken
argument_list|(
literal|"token"
argument_list|)
operator|.
name|setSecret
argument_list|(
literal|"secret"
argument_list|)
operator|.
name|setRaw
argument_list|(
literal|"raw"
argument_list|)
operator|.
name|setExpiresAt
argument_list|(
literal|12345L
argument_list|)
operator|.
name|setProviderId
argument_list|(
literal|""
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|byte
index|[]
name|serializedWithNull
init|=
name|s
operator|.
name|serialize
argument_list|(
name|tokenWithNull
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|OAuthTokenProto
operator|.
name|parseFrom
argument_list|(
name|serializedWithNull
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedProto
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|s
operator|.
name|deserialize
argument_list|(
name|serializedWithNull
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tokenWithNull
argument_list|)
expr_stmt|;
name|OAuthToken
name|tokenWithEmptyString
init|=
operator|new
name|OAuthToken
argument_list|(
literal|"token"
argument_list|,
literal|"secret"
argument_list|,
literal|"raw"
argument_list|,
literal|12345L
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|tokenWithEmptyString
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tokenWithNull
argument_list|)
expr_stmt|;
name|byte
index|[]
name|serializedWithEmptyString
init|=
name|s
operator|.
name|serialize
argument_list|(
name|tokenWithEmptyString
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|OAuthTokenProto
operator|.
name|parseFrom
argument_list|(
name|serializedWithEmptyString
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedProto
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|s
operator|.
name|deserialize
argument_list|(
name|serializedWithEmptyString
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|tokenWithNull
argument_list|)
expr_stmt|;
block|}
comment|/**    * See {@link com.google.gerrit.server.cache.testing.SerializedClassSubject} for background and    * what to do if this test fails.    */
annotation|@
name|Test
DECL|method|oAuthTokenFields ()
specifier|public
name|void
name|oAuthTokenFields
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThatSerializedClass
argument_list|(
name|OAuthToken
operator|.
name|class
argument_list|)
operator|.
name|hasFields
argument_list|(
name|ImmutableMap
operator|.
expr|<
name|String
argument_list|,
name|Type
operator|>
name|builder
argument_list|()
operator|.
name|put
argument_list|(
literal|"token"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
literal|"secret"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
literal|"raw"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
literal|"expiresAt"
argument_list|,
name|long
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
literal|"providerId"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

