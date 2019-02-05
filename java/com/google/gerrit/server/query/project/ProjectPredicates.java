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
DECL|package|com.google.gerrit.server.query.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
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
name|extensions
operator|.
name|client
operator|.
name|ProjectState
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
name|project
operator|.
name|ProjectData
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
name|project
operator|.
name|ProjectField
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
name|project
operator|.
name|ProjectPredicate
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_class
DECL|class|ProjectPredicates
specifier|public
class|class
name|ProjectPredicates
block|{
DECL|method|name (Project.NameKey nameKey)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ProjectData
argument_list|>
name|name
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
DECL|method|parent (Project.NameKey parentNameKey)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ProjectData
argument_list|>
name|parent
parameter_list|(
name|Project
operator|.
name|NameKey
name|parentNameKey
parameter_list|)
block|{
return|return
operator|new
name|ProjectPredicate
argument_list|(
name|ProjectField
operator|.
name|PARENT_NAME
argument_list|,
name|parentNameKey
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|inname (String name)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ProjectData
argument_list|>
name|inname
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ProjectPredicate
argument_list|(
name|ProjectField
operator|.
name|NAME_PART
argument_list|,
name|name
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
argument_list|)
return|;
block|}
DECL|method|description (String description)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ProjectData
argument_list|>
name|description
parameter_list|(
name|String
name|description
parameter_list|)
block|{
return|return
operator|new
name|ProjectPredicate
argument_list|(
name|ProjectField
operator|.
name|DESCRIPTION
argument_list|,
name|description
argument_list|)
return|;
block|}
DECL|method|state (ProjectState state)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ProjectData
argument_list|>
name|state
parameter_list|(
name|ProjectState
name|state
parameter_list|)
block|{
return|return
operator|new
name|ProjectPredicate
argument_list|(
name|ProjectField
operator|.
name|STATE
argument_list|,
name|state
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|ProjectPredicates ()
specifier|private
name|ProjectPredicates
parameter_list|()
block|{}
block|}
end_class

end_unit

