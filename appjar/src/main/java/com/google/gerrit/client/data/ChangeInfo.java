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
name|client
operator|.
name|reviewdb
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
DECL|field|owner
specifier|protected
name|Account
operator|.
name|Id
name|owner
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
DECL|field|project
specifier|protected
name|ProjectInfo
name|project
decl_stmt|;
DECL|field|starred
specifier|protected
name|boolean
name|starred
decl_stmt|;
DECL|field|lastUpdatedOn
specifier|protected
name|Timestamp
name|lastUpdatedOn
decl_stmt|;
DECL|field|sortKey
specifier|protected
name|String
name|sortKey
decl_stmt|;
DECL|method|ChangeInfo ()
specifier|protected
name|ChangeInfo
parameter_list|()
block|{   }
DECL|method|ChangeInfo (final Change c, final AccountInfoCacheFactory acc)
specifier|public
name|ChangeInfo
parameter_list|(
specifier|final
name|Change
name|c
parameter_list|,
specifier|final
name|AccountInfoCacheFactory
name|acc
parameter_list|)
block|{
name|id
operator|=
name|c
operator|.
name|getId
argument_list|()
expr_stmt|;
name|owner
operator|=
name|c
operator|.
name|getOwner
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
name|lastUpdatedOn
operator|=
name|c
operator|.
name|getLastUpdatedOn
argument_list|()
expr_stmt|;
name|sortKey
operator|=
name|c
operator|.
name|getSortKey
argument_list|()
expr_stmt|;
name|acc
operator|.
name|want
argument_list|(
name|owner
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
DECL|method|getOwner ()
specifier|public
name|Account
operator|.
name|Id
name|getOwner
parameter_list|()
block|{
return|return
name|owner
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
DECL|method|isStarred ()
specifier|public
name|boolean
name|isStarred
parameter_list|()
block|{
return|return
name|starred
return|;
block|}
DECL|method|setStarred (final boolean s)
specifier|public
name|void
name|setStarred
parameter_list|(
specifier|final
name|boolean
name|s
parameter_list|)
block|{
name|starred
operator|=
name|s
expr_stmt|;
block|}
DECL|method|getLastUpdatedOn ()
specifier|public
name|java
operator|.
name|sql
operator|.
name|Timestamp
name|getLastUpdatedOn
parameter_list|()
block|{
return|return
name|lastUpdatedOn
return|;
block|}
DECL|method|getSortKey ()
specifier|public
name|String
name|getSortKey
parameter_list|()
block|{
return|return
name|sortKey
return|;
block|}
block|}
end_class

end_unit

