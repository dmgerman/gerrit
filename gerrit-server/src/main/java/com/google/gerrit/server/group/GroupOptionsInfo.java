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
name|common
operator|.
name|data
operator|.
name|GroupDescriptions
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

begin_class
DECL|class|GroupOptionsInfo
specifier|public
class|class
name|GroupOptionsInfo
block|{
DECL|field|visibleToAll
specifier|public
name|Boolean
name|visibleToAll
decl_stmt|;
DECL|method|GroupOptionsInfo (GroupDescription.Basic group)
specifier|public
name|GroupOptionsInfo
parameter_list|(
name|GroupDescription
operator|.
name|Basic
name|group
parameter_list|)
block|{
name|AccountGroup
name|ag
init|=
name|GroupDescriptions
operator|.
name|toAccountGroup
argument_list|(
name|group
argument_list|)
decl_stmt|;
name|visibleToAll
operator|=
name|ag
operator|!=
literal|null
operator|&&
name|ag
operator|.
name|isVisibleToAll
argument_list|()
condition|?
literal|true
else|:
literal|null
expr_stmt|;
block|}
DECL|method|GroupOptionsInfo (AccountGroup group)
specifier|public
name|GroupOptionsInfo
parameter_list|(
name|AccountGroup
name|group
parameter_list|)
block|{
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
block|}
block|}
end_class

end_unit

