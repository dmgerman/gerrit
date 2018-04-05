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
DECL|package|com.google.gerrit.index.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|project
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
name|collect
operator|.
name|ImmutableList
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
name|Project
import|;
end_import

begin_class
DECL|class|ProjectData
specifier|public
class|class
name|ProjectData
block|{
DECL|field|project
specifier|private
specifier|final
name|Project
name|project
decl_stmt|;
DECL|field|ancestors
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|ancestors
decl_stmt|;
DECL|method|ProjectData (Project project, Iterable<Project.NameKey> ancestors)
specifier|public
name|ProjectData
parameter_list|(
name|Project
name|project
parameter_list|,
name|Iterable
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|ancestors
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|ancestors
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|ancestors
argument_list|)
expr_stmt|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
DECL|method|getAncestors ()
specifier|public
name|ImmutableList
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|getAncestors
parameter_list|()
block|{
return|return
name|ancestors
return|;
block|}
block|}
end_class

end_unit
