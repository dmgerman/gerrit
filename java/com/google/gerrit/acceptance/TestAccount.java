begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|ImmutableList
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
name|Streams
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
name|net
operator|.
name|InetAddresses
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
name|common
operator|.
name|Nullable
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
name|entities
operator|.
name|Account
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
name|mail
operator|.
name|Address
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|utils
operator|.
name|URIBuilder
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
name|PersonIdent
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|TestAccount
specifier|public
specifier|abstract
class|class
name|TestAccount
block|{
DECL|method|ids (Iterable<TestAccount> accounts)
specifier|public
specifier|static
name|ImmutableList
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ids
parameter_list|(
name|Iterable
argument_list|<
name|TestAccount
argument_list|>
name|accounts
parameter_list|)
block|{
return|return
name|Streams
operator|.
name|stream
argument_list|(
name|accounts
argument_list|)
operator|.
name|map
argument_list|(
name|TestAccount
operator|::
name|id
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|names (Iterable<TestAccount> accounts)
specifier|public
specifier|static
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|names
parameter_list|(
name|Iterable
argument_list|<
name|TestAccount
argument_list|>
name|accounts
parameter_list|)
block|{
return|return
name|Streams
operator|.
name|stream
argument_list|(
name|accounts
argument_list|)
operator|.
name|map
argument_list|(
name|TestAccount
operator|::
name|fullName
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|names (TestAccount... accounts)
specifier|public
specifier|static
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|names
parameter_list|(
name|TestAccount
modifier|...
name|accounts
parameter_list|)
block|{
return|return
name|names
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|accounts
argument_list|)
argument_list|)
return|;
block|}
DECL|method|create ( Account.Id id, @Nullable String username, @Nullable String email, @Nullable String fullName, @Nullable String httpPassword)
specifier|static
name|TestAccount
name|create
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
annotation|@
name|Nullable
name|String
name|username
parameter_list|,
annotation|@
name|Nullable
name|String
name|email
parameter_list|,
annotation|@
name|Nullable
name|String
name|fullName
parameter_list|,
annotation|@
name|Nullable
name|String
name|httpPassword
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_TestAccount
argument_list|(
name|id
argument_list|,
name|username
argument_list|,
name|email
argument_list|,
name|fullName
argument_list|,
name|httpPassword
argument_list|)
return|;
block|}
DECL|method|id ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|id
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|username ()
specifier|public
specifier|abstract
name|String
name|username
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|email ()
specifier|public
specifier|abstract
name|String
name|email
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|fullName ()
specifier|public
specifier|abstract
name|String
name|fullName
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|httpPassword ()
specifier|public
specifier|abstract
name|String
name|httpPassword
parameter_list|()
function_decl|;
DECL|method|newIdent ()
specifier|public
name|PersonIdent
name|newIdent
parameter_list|()
block|{
return|return
operator|new
name|PersonIdent
argument_list|(
name|fullName
argument_list|()
argument_list|,
name|email
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getHttpUrl (GerritServer server)
specifier|public
name|String
name|getHttpUrl
parameter_list|(
name|GerritServer
name|server
parameter_list|)
block|{
name|InetSocketAddress
name|addr
init|=
name|server
operator|.
name|getHttpAddress
argument_list|()
decl_stmt|;
return|return
operator|new
name|URIBuilder
argument_list|()
operator|.
name|setScheme
argument_list|(
literal|"http"
argument_list|)
operator|.
name|setUserInfo
argument_list|(
name|username
argument_list|()
argument_list|,
name|httpPassword
argument_list|()
argument_list|)
operator|.
name|setHost
argument_list|(
name|InetAddresses
operator|.
name|toUriString
argument_list|(
name|addr
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setPort
argument_list|(
name|addr
operator|.
name|getPort
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getEmailAddress ()
specifier|public
name|Address
name|getEmailAddress
parameter_list|()
block|{
comment|// Address is weird enough that it's safer and clearer to create a new instance in a
comment|// non-abstract method rather than, say, having an abstract emailAddress() as part of this
comment|// AutoValue class. Specifically:
comment|//  * Email is not specified as @Nullable in Address, but it is nullable in this class. If this
comment|//    is a problem, at least it's a problem only for users of TestAccount that actually call
comment|//    emailAddress().
comment|//  * Address#equals only considers email, not name, whereas TestAccount#equals should include
comment|//    name.
return|return
operator|new
name|Address
argument_list|(
name|fullName
argument_list|()
argument_list|,
name|email
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

