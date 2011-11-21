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
name|data
operator|.
name|GroupDetail
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
name|NoSuchGroupException
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
name|VisibleGroups
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|MyGroupsFactory
class|class
name|MyGroupsFactory
extends|extends
name|Handler
argument_list|<
name|List
argument_list|<
name|GroupDetail
argument_list|>
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create ()
name|MyGroupsFactory
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|visibleGroupsFactory
specifier|private
specifier|final
name|VisibleGroups
operator|.
name|Factory
name|visibleGroupsFactory
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
annotation|@
name|Inject
DECL|method|MyGroupsFactory (final VisibleGroups.Factory visibleGroupsFactory, final IdentifiedUser user)
name|MyGroupsFactory
parameter_list|(
specifier|final
name|VisibleGroups
operator|.
name|Factory
name|visibleGroupsFactory
parameter_list|,
specifier|final
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|visibleGroupsFactory
operator|=
name|visibleGroupsFactory
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|List
argument_list|<
name|GroupDetail
argument_list|>
name|call
parameter_list|()
throws|throws
name|OrmException
throws|,
name|NoSuchGroupException
block|{
specifier|final
name|VisibleGroups
name|visibleGroups
init|=
name|visibleGroupsFactory
operator|.
name|create
argument_list|()
decl_stmt|;
return|return
name|visibleGroups
operator|.
name|get
argument_list|(
name|user
argument_list|)
operator|.
name|getGroups
argument_list|()
return|;
block|}
block|}
end_class

end_unit

