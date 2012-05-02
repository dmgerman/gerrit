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
DECL|package|com.google.gerrit.server.auth.ldap
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
name|ldap
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|HOURS
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
name|base
operator|.
name|Optional
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
name|registration
operator|.
name|DynamicSet
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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|account
operator|.
name|GroupBackend
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
name|account
operator|.
name|Realm
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
name|CacheModule
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
name|Scopes
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
name|TypeLiteral
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|LdapModule
specifier|public
class|class
name|LdapModule
extends|extends
name|CacheModule
block|{
DECL|field|USERNAME_CACHE
specifier|static
specifier|final
name|String
name|USERNAME_CACHE
init|=
literal|"ldap_usernames"
decl_stmt|;
DECL|field|GROUP_CACHE
specifier|static
specifier|final
name|String
name|GROUP_CACHE
init|=
literal|"ldap_groups"
decl_stmt|;
DECL|field|GROUP_EXIST_CACHE
specifier|static
specifier|final
name|String
name|GROUP_EXIST_CACHE
init|=
literal|"ldap_group_existence"
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|cache
argument_list|(
name|GROUP_CACHE
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|expireAfterWrite
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
operator|.
name|loader
argument_list|(
name|LdapRealm
operator|.
name|MemberLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|USERNAME_CACHE
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Optional
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|loader
argument_list|(
name|LdapRealm
operator|.
name|UserLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|cache
argument_list|(
name|GROUP_EXIST_CACHE
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|expireAfterWrite
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
operator|.
name|loader
argument_list|(
name|LdapRealm
operator|.
name|ExistenceLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Realm
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|LdapRealm
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|Scopes
operator|.
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Helper
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicSet
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|GroupBackend
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|LdapGroupBackend
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

