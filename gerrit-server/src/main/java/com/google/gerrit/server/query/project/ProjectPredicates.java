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
name|index
operator|.
name|FieldDef
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
name|IndexPredicate
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|server
operator|.
name|project
operator|.
name|ProjectState
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
name|ProjectState
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
DECL|class|ProjectPredicate
specifier|static
class|class
name|ProjectPredicate
extends|extends
name|IndexPredicate
argument_list|<
name|ProjectState
argument_list|>
block|{
DECL|method|ProjectPredicate (FieldDef<ProjectState, ?> def, String value)
name|ProjectPredicate
parameter_list|(
name|FieldDef
argument_list|<
name|ProjectState
argument_list|,
name|?
argument_list|>
name|def
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|def
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|ProjectPredicates ()
specifier|private
name|ProjectPredicates
parameter_list|()
block|{}
block|}
end_class

end_unit

