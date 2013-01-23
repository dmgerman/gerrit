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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
package|;
end_package

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
name|Strings
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
name|data
operator|.
name|GroupDescription
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
name|util
operator|.
name|Url
import|;
end_import

begin_class
DECL|class|GroupInfo
specifier|public
class|class
name|GroupInfo
block|{
DECL|field|kind
specifier|final
name|String
name|kind
init|=
literal|"gerritcodereview#group"
decl_stmt|;
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|name
specifier|public
name|String
name|name
decl_stmt|;
DECL|field|url
specifier|public
name|String
name|url
decl_stmt|;
DECL|field|visibleToAll
specifier|public
name|Boolean
name|visibleToAll
decl_stmt|;
comment|// These fields are only supplied for internal groups.
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|groupId
specifier|public
name|Integer
name|groupId
decl_stmt|;
DECL|field|ownerId
specifier|public
name|String
name|ownerId
decl_stmt|;
DECL|method|GroupInfo (GroupDescription.Basic group)
specifier|public
name|GroupInfo
parameter_list|(
name|GroupDescription
operator|.
name|Basic
name|group
parameter_list|)
block|{
name|id
operator|=
name|Url
operator|.
name|encode
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|group
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|url
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|group
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|visibleToAll
operator|=
name|group
operator|.
name|isVisibleToAll
argument_list|()
condition|?
literal|true
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|group
operator|instanceof
name|GroupDescription
operator|.
name|Internal
condition|)
block|{
name|set
argument_list|(
operator|(
operator|(
name|GroupDescription
operator|.
name|Internal
operator|)
name|group
operator|)
operator|.
name|getAccountGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|set (AccountGroup d)
specifier|private
name|void
name|set
parameter_list|(
name|AccountGroup
name|d
parameter_list|)
block|{
name|description
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|d
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|groupId
operator|=
name|d
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|ownerId
operator|=
name|d
operator|.
name|getOwnerGroupUUID
argument_list|()
operator|!=
literal|null
condition|?
name|Url
operator|.
name|encode
argument_list|(
name|d
operator|.
name|getOwnerGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

