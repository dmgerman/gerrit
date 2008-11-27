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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|client
operator|.
name|reviewdb
operator|.
name|Change
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

begin_class
DECL|class|ChangeInfo
specifier|public
class|class
name|ChangeInfo
block|{
DECL|field|id
specifier|protected
name|Change
operator|.
name|Id
name|id
decl_stmt|;
DECL|field|subject
specifier|protected
name|String
name|subject
decl_stmt|;
DECL|field|status
specifier|protected
name|Change
operator|.
name|Status
name|status
decl_stmt|;
DECL|field|owner
specifier|protected
name|AccountInfo
name|owner
decl_stmt|;
DECL|field|project
specifier|protected
name|ProjectInfo
name|project
decl_stmt|;
DECL|method|ChangeInfo ()
specifier|protected
name|ChangeInfo
parameter_list|()
block|{   }
DECL|method|ChangeInfo (final Change c, final AccountCache accounts)
specifier|public
name|ChangeInfo
parameter_list|(
specifier|final
name|Change
name|c
parameter_list|,
specifier|final
name|AccountCache
name|accounts
parameter_list|)
throws|throws
name|OrmException
block|{
name|id
operator|=
name|c
operator|.
name|getKey
argument_list|()
expr_stmt|;
name|subject
operator|=
name|c
operator|.
name|getSubject
argument_list|()
expr_stmt|;
name|status
operator|=
name|c
operator|.
name|getStatus
argument_list|()
expr_stmt|;
name|owner
operator|=
operator|new
name|AccountInfo
argument_list|(
name|accounts
operator|.
name|get
argument_list|(
name|c
operator|.
name|getOwner
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|project
operator|=
operator|new
name|ProjectInfo
argument_list|(
name|c
operator|.
name|getDest
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|Change
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
DECL|method|getStatus ()
specifier|public
name|Change
operator|.
name|Status
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
DECL|method|getOwner ()
specifier|public
name|AccountInfo
name|getOwner
parameter_list|()
block|{
return|return
name|owner
return|;
block|}
DECL|method|getProject ()
specifier|public
name|ProjectInfo
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
block|}
end_class

end_unit

