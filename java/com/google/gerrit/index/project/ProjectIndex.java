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
name|gerrit
operator|.
name|entities
operator|.
name|Project
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
name|index
operator|.
name|Index
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
name|index
operator|.
name|IndexDefinition
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
name|index
operator|.
name|query
operator|.
name|Predicate
import|;
end_import

begin_interface
DECL|interface|ProjectIndex
specifier|public
interface|interface
name|ProjectIndex
extends|extends
name|Index
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|ProjectData
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
extends|extends
name|IndexDefinition
operator|.
name|IndexFactory
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|ProjectData
argument_list|,
name|ProjectIndex
argument_list|>
block|{}
annotation|@
name|Override
DECL|method|keyPredicate (Project.NameKey nameKey)
specifier|default
name|Predicate
argument_list|<
name|ProjectData
argument_list|>
name|keyPredicate
parameter_list|(
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
block|{
return|return
operator|new
name|ProjectPredicate
argument_list|(
name|ProjectField
operator|.
name|NAME
argument_list|,
name|nameKey
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

