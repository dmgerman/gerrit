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
DECL|package|com.google.gerrit.httpd.rpc.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|account
package|;
end_package

begin_import
import|import static
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
name|AccountExternalId
operator|.
name|SCHEME_USERNAME
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
name|httpd
operator|.
name|WebSession
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
name|httpd
operator|.
name|rpc
operator|.
name|Handler
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
name|AccountExternalId
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
name|server
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
name|IdentifiedUser
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
name|AuthConfig
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
name|server
operator|.
name|OrmException
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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|ExternalIdDetailFactory
class|class
name|ExternalIdDetailFactory
extends|extends
name|Handler
argument_list|<
name|List
argument_list|<
name|AccountExternalId
argument_list|>
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ()
name|ExternalIdDetailFactory
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|session
specifier|private
specifier|final
name|WebSession
name|session
decl_stmt|;
annotation|@
name|Inject
DECL|method|ExternalIdDetailFactory (final ReviewDb db, final IdentifiedUser user, final AuthConfig authConfig, final WebSession session)
name|ExternalIdDetailFactory
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|IdentifiedUser
name|user
parameter_list|,
specifier|final
name|AuthConfig
name|authConfig
parameter_list|,
specifier|final
name|WebSession
name|session
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|call
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|AccountExternalId
operator|.
name|Key
name|last
init|=
name|session
operator|.
name|getLastLoginExternalId
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|ids
init|=
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|byAccount
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountExternalId
name|e
range|:
name|ids
control|)
block|{
name|e
operator|.
name|setTrusted
argument_list|(
name|authConfig
operator|.
name|isIdentityTrustable
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|e
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// The identity can be deleted only if its not the one used to
comment|// establish this web session, and if only if an identity was
comment|// actually used to establish this web session.
comment|//
if|if
condition|(
name|e
operator|.
name|isScheme
argument_list|(
name|SCHEME_USERNAME
argument_list|)
condition|)
block|{
name|e
operator|.
name|setCanDelete
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|e
operator|.
name|setCanDelete
argument_list|(
name|last
operator|!=
literal|null
operator|&&
operator|!
name|last
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ids
return|;
block|}
block|}
end_class

end_unit

