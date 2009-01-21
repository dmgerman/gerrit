begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

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
name|Column
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
name|CompoundKey
import|;
end_import

begin_comment
comment|/** An {@link Account} interested in a {@link Project}. */
end_comment

begin_class
DECL|class|AccountProjectWatch
specifier|public
specifier|final
class|class
name|AccountProjectWatch
block|{
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|CompoundKey
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
block|{
annotation|@
name|Column
DECL|field|accountId
specifier|protected
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Column
DECL|field|projectId
specifier|protected
name|Project
operator|.
name|Id
name|projectId
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|accountId
operator|=
operator|new
name|Account
operator|.
name|Id
argument_list|()
expr_stmt|;
name|projectId
operator|=
operator|new
name|Project
operator|.
name|Id
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (final Account.Id a, final Project.Id g)
specifier|public
name|Key
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|a
parameter_list|,
specifier|final
name|Project
operator|.
name|Id
name|g
parameter_list|)
block|{
name|accountId
operator|=
name|a
expr_stmt|;
name|projectId
operator|=
name|g
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Account
operator|.
name|Id
name|getParentKey
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
annotation|@
name|Override
DECL|method|members ()
specifier|public
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
name|members
parameter_list|()
block|{
return|return
operator|new
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|projectId
block|}
empty_stmt|;
block|}
block|}
annotation|@
name|Column
argument_list|(
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
comment|/** Automatically send email notifications of new changes? */
annotation|@
name|Column
DECL|field|notifyNewChanges
specifier|protected
name|boolean
name|notifyNewChanges
decl_stmt|;
comment|/** Automatically receive comments published to this project */
annotation|@
name|Column
DECL|field|notifyAllComments
specifier|protected
name|boolean
name|notifyAllComments
decl_stmt|;
DECL|method|AccountProjectWatch ()
specifier|protected
name|AccountProjectWatch
parameter_list|()
block|{   }
DECL|method|AccountProjectWatch (final AccountProjectWatch.Key k)
specifier|public
name|AccountProjectWatch
parameter_list|(
specifier|final
name|AccountProjectWatch
operator|.
name|Key
name|k
parameter_list|)
block|{
name|key
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|AccountProjectWatch
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|key
operator|.
name|accountId
return|;
block|}
DECL|method|getProjectId ()
specifier|public
name|Project
operator|.
name|Id
name|getProjectId
parameter_list|()
block|{
return|return
name|key
operator|.
name|projectId
return|;
block|}
DECL|method|isNotifyNewChanges ()
specifier|public
name|boolean
name|isNotifyNewChanges
parameter_list|()
block|{
return|return
name|notifyNewChanges
return|;
block|}
DECL|method|setNotifyNewChanges (final boolean a)
specifier|public
name|void
name|setNotifyNewChanges
parameter_list|(
specifier|final
name|boolean
name|a
parameter_list|)
block|{
name|notifyNewChanges
operator|=
name|a
expr_stmt|;
block|}
DECL|method|isNotifyAllComments ()
specifier|public
name|boolean
name|isNotifyAllComments
parameter_list|()
block|{
return|return
name|notifyAllComments
return|;
block|}
DECL|method|setNotifyAllComments (final boolean a)
specifier|public
name|void
name|setNotifyAllComments
parameter_list|(
specifier|final
name|boolean
name|a
parameter_list|)
block|{
name|notifyAllComments
operator|=
name|a
expr_stmt|;
block|}
block|}
end_class

end_unit

