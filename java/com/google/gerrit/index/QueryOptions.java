begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

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
name|ImmutableSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|Ints
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
annotation|@
name|AutoValue
DECL|class|QueryOptions
specifier|public
specifier|abstract
class|class
name|QueryOptions
block|{
DECL|method|create (IndexConfig config, int start, int limit, Set<String> fields)
specifier|public
specifier|static
name|QueryOptions
name|create
parameter_list|(
name|IndexConfig
name|config
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|limit
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|start
operator|>=
literal|0
argument_list|,
literal|"start must be nonnegative: %s"
argument_list|,
name|start
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|limit
operator|>
literal|0
argument_list|,
literal|"limit must be positive: %s"
argument_list|,
name|limit
argument_list|)
expr_stmt|;
return|return
operator|new
name|AutoValue_QueryOptions
argument_list|(
name|config
argument_list|,
name|start
argument_list|,
name|limit
argument_list|,
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|fields
argument_list|)
argument_list|)
return|;
block|}
DECL|method|convertForBackend ()
specifier|public
name|QueryOptions
name|convertForBackend
parameter_list|()
block|{
comment|// Increase the limit rather than skipping, since we don't know how many
comment|// skipped results would have been filtered out by the enclosing AndSource.
name|int
name|backendLimit
init|=
name|config
argument_list|()
operator|.
name|maxLimit
argument_list|()
decl_stmt|;
name|int
name|limit
init|=
name|Ints
operator|.
name|saturatedCast
argument_list|(
operator|(
name|long
operator|)
name|limit
argument_list|()
operator|+
name|start
argument_list|()
argument_list|)
decl_stmt|;
name|limit
operator|=
name|Math
operator|.
name|min
argument_list|(
name|limit
argument_list|,
name|backendLimit
argument_list|)
expr_stmt|;
return|return
name|create
argument_list|(
name|config
argument_list|()
argument_list|,
literal|0
argument_list|,
name|limit
argument_list|,
name|fields
argument_list|()
argument_list|)
return|;
block|}
DECL|method|config ()
specifier|public
specifier|abstract
name|IndexConfig
name|config
parameter_list|()
function_decl|;
DECL|method|start ()
specifier|public
specifier|abstract
name|int
name|start
parameter_list|()
function_decl|;
DECL|method|limit ()
specifier|public
specifier|abstract
name|int
name|limit
parameter_list|()
function_decl|;
DECL|method|fields ()
specifier|public
specifier|abstract
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|()
function_decl|;
DECL|method|withLimit (int newLimit)
specifier|public
name|QueryOptions
name|withLimit
parameter_list|(
name|int
name|newLimit
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|config
argument_list|()
argument_list|,
name|start
argument_list|()
argument_list|,
name|newLimit
argument_list|,
name|fields
argument_list|()
argument_list|)
return|;
block|}
DECL|method|withStart (int newStart)
specifier|public
name|QueryOptions
name|withStart
parameter_list|(
name|int
name|newStart
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|config
argument_list|()
argument_list|,
name|newStart
argument_list|,
name|limit
argument_list|()
argument_list|,
name|fields
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

