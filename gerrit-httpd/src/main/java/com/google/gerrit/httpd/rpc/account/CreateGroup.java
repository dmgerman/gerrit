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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|errors
operator|.
name|NameAlreadyUsedException
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
name|errors
operator|.
name|PermissionDeniedException
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
name|account
operator|.
name|PerformCreateGroup
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
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

begin_class
DECL|class|CreateGroup
class|class
name|CreateGroup
extends|extends
name|Handler
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (String groupName)
name|CreateGroup
name|create
parameter_list|(
name|String
name|groupName
parameter_list|)
function_decl|;
block|}
DECL|field|performCreateGroupFactory
specifier|private
specifier|final
name|PerformCreateGroup
operator|.
name|Factory
name|performCreateGroupFactory
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|groupName
specifier|private
specifier|final
name|String
name|groupName
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateGroup (final PerformCreateGroup.Factory performCreateGroupFactory, final IdentifiedUser user, @Assisted final String groupName)
name|CreateGroup
parameter_list|(
specifier|final
name|PerformCreateGroup
operator|.
name|Factory
name|performCreateGroupFactory
parameter_list|,
specifier|final
name|IdentifiedUser
name|user
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|String
name|groupName
parameter_list|)
block|{
name|this
operator|.
name|performCreateGroupFactory
operator|=
name|performCreateGroupFactory
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|groupName
operator|=
name|groupName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|call
parameter_list|()
throws|throws
name|OrmException
throws|,
name|NameAlreadyUsedException
throws|,
name|PermissionDeniedException
block|{
specifier|final
name|PerformCreateGroup
name|performCreateGroup
init|=
name|performCreateGroupFactory
operator|.
name|create
argument_list|()
decl_stmt|;
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|user
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
return|return
name|performCreateGroup
operator|.
name|createGroup
argument_list|(
name|groupName
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|me
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

