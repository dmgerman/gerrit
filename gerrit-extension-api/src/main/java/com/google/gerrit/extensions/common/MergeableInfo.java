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
name|SubmitType
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
DECL|class|MergeableInfo
specifier|public
class|class
name|MergeableInfo
block|{
DECL|field|submitType
specifier|public
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|strategy
specifier|public
name|String
name|strategy
decl_stmt|;
DECL|field|mergeable
specifier|public
name|boolean
name|mergeable
decl_stmt|;
DECL|field|commitMerged
specifier|public
name|boolean
name|commitMerged
decl_stmt|;
DECL|field|contentMerged
specifier|public
name|boolean
name|contentMerged
decl_stmt|;
DECL|field|conflicts
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|conflicts
decl_stmt|;
DECL|field|mergeableInto
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|mergeableInto
decl_stmt|;
block|}
end_class

end_unit

