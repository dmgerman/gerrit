begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.contact
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|contact
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|ReviewDb
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
name|config
operator|.
name|GerritServerConfig
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
name|config
operator|.
name|SitePaths
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_comment
comment|/** Creates the {@link ContactStore} based on the configuration. */
end_comment

begin_class
DECL|class|ContactStoreProvider
specifier|public
class|class
name|ContactStoreProvider
implements|implements
name|Provider
argument_list|<
name|ContactStore
argument_list|>
block|{
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
annotation|@
name|Inject
DECL|method|ContactStoreProvider (@erritServerConfig final Config config, final SitePaths site, final SchemaFactory<ReviewDb> schema)
name|ContactStoreProvider
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|config
parameter_list|,
specifier|final
name|SitePaths
name|site
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ContactStore
name|get
parameter_list|()
block|{
specifier|final
name|String
name|url
init|=
name|config
operator|.
name|getString
argument_list|(
literal|"contactstore"
argument_list|,
literal|null
argument_list|,
literal|"url"
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|NoContactStore
argument_list|()
return|;
block|}
if|if
condition|(
operator|!
name|havePGP
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"BouncyCastle PGP not installed; "
operator|+
literal|" needed to encrypt contact information"
argument_list|)
throw|;
block|}
specifier|final
name|URL
name|storeUrl
decl_stmt|;
try|try
block|{
name|storeUrl
operator|=
operator|new
name|URL
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Invalid contactstore.url: "
operator|+
name|url
argument_list|,
name|e
argument_list|)
throw|;
block|}
specifier|final
name|String
name|storeAPPSEC
init|=
name|config
operator|.
name|getString
argument_list|(
literal|"contactstore"
argument_list|,
literal|null
argument_list|,
literal|"appsec"
argument_list|)
decl_stmt|;
specifier|final
name|File
name|pubkey
init|=
name|site
operator|.
name|contact_information_pub
decl_stmt|;
if|if
condition|(
operator|!
name|pubkey
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"PGP public key file \""
operator|+
name|pubkey
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"\" not found"
argument_list|)
throw|;
block|}
return|return
operator|new
name|EncryptedContactStore
argument_list|(
name|storeUrl
argument_list|,
name|storeAPPSEC
argument_list|,
name|pubkey
argument_list|,
name|schema
argument_list|)
return|;
block|}
DECL|method|havePGP ()
specifier|private
specifier|static
name|boolean
name|havePGP
parameter_list|()
block|{
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
name|PGPPublicKey
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|noBouncyCastle
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|noBouncyCastle
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

