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
name|InvalidNameException
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
name|PerformRenameGroup
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

begin_class
DECL|class|RenameGroup
class|class
name|RenameGroup
extends|extends
name|Handler
argument_list|<
name|GroupDetail
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (AccountGroup.Id id, String newName)
name|RenameGroup
name|create
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|,
name|String
name|newName
parameter_list|)
function_decl|;
block|}
DECL|field|performRenameGroupFactory
specifier|private
specifier|final
name|PerformRenameGroup
operator|.
name|Factory
name|performRenameGroupFactory
decl_stmt|;
DECL|field|groupId
specifier|private
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
decl_stmt|;
DECL|field|newName
specifier|private
specifier|final
name|String
name|newName
decl_stmt|;
annotation|@
name|Inject
DECL|method|RenameGroup (final PerformRenameGroup.Factory performRenameGroupFactory, @Assisted final AccountGroup.Id groupId, @Assisted final String newName)
name|RenameGroup
parameter_list|(
specifier|final
name|PerformRenameGroup
operator|.
name|Factory
name|performRenameGroupFactory
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|String
name|newName
parameter_list|)
block|{
name|this
operator|.
name|performRenameGroupFactory
operator|=
name|performRenameGroupFactory
expr_stmt|;
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
name|this
operator|.
name|newName
operator|=
name|newName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|GroupDetail
name|call
parameter_list|()
throws|throws
name|OrmException
throws|,
name|NameAlreadyUsedException
throws|,
name|NoSuchGroupException
throws|,
name|InvalidNameException
block|{
return|return
name|performRenameGroupFactory
operator|.
name|create
argument_list|()
operator|.
name|renameGroup
argument_list|(
name|groupId
argument_list|,
name|newName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

