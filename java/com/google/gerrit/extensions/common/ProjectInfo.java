begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|ProjectState
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ProjectInfo
specifier|public
class|class
name|ProjectInfo
block|{
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
DECL|field|parent
specifier|public
name|String
name|parent
decl_stmt|;
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|state
specifier|public
name|ProjectState
name|state
decl_stmt|;
DECL|field|branches
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|branches
decl_stmt|;
DECL|field|webLinks
specifier|public
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
decl_stmt|;
DECL|field|labels
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|LabelTypeInfo
argument_list|>
name|labels
decl_stmt|;
block|}
end_class

end_unit

