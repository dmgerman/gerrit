begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|changes
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
DECL|class|IncludedInInfo
specifier|public
class|class
name|IncludedInInfo
block|{
DECL|field|branches
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|branches
decl_stmt|;
DECL|field|tags
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|tags
decl_stmt|;
DECL|field|external
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|external
decl_stmt|;
DECL|method|IncludedInInfo ( List<String> branches, List<String> tags, Map<String, Collection<String>> external)
specifier|public
name|IncludedInInfo
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|branches
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|tags
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|external
parameter_list|)
block|{
name|this
operator|.
name|branches
operator|=
name|branches
expr_stmt|;
name|this
operator|.
name|tags
operator|=
name|tags
expr_stmt|;
name|this
operator|.
name|external
operator|=
name|external
expr_stmt|;
block|}
block|}
end_class

end_unit

