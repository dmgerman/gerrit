begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
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
name|extensions
operator|.
name|client
operator|.
name|UiType
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
DECL|class|GerritInfo
specifier|public
class|class
name|GerritInfo
block|{
DECL|field|allProjects
specifier|public
name|String
name|allProjects
decl_stmt|;
DECL|field|allUsers
specifier|public
name|String
name|allUsers
decl_stmt|;
DECL|field|docSearch
specifier|public
name|Boolean
name|docSearch
decl_stmt|;
DECL|field|docUrl
specifier|public
name|String
name|docUrl
decl_stmt|;
DECL|field|editGpgKeys
specifier|public
name|Boolean
name|editGpgKeys
decl_stmt|;
DECL|field|reportBugUrl
specifier|public
name|String
name|reportBugUrl
decl_stmt|;
DECL|field|reportBugText
specifier|public
name|String
name|reportBugText
decl_stmt|;
DECL|field|webUis
specifier|public
name|Set
argument_list|<
name|UiType
argument_list|>
name|webUis
decl_stmt|;
DECL|field|primaryWeblinkName
specifier|public
name|String
name|primaryWeblinkName
decl_stmt|;
block|}
end_class

end_unit

