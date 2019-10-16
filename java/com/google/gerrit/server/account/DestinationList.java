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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|Lists
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
name|MultimapBuilder
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
name|SetMultimap
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
name|Sets
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
name|entities
operator|.
name|BranchNameKey
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
name|server
operator|.
name|git
operator|.
name|ValidationError
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
name|git
operator|.
name|meta
operator|.
name|TabFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Set
import|;
end_import

begin_class
DECL|class|DestinationList
specifier|public
class|class
name|DestinationList
extends|extends
name|TabFile
block|{
DECL|field|DIR_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DIR_NAME
init|=
literal|"destinations"
decl_stmt|;
DECL|field|destinations
specifier|private
name|SetMultimap
argument_list|<
name|String
argument_list|,
name|BranchNameKey
argument_list|>
name|destinations
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|hashSetValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
DECL|method|getDestinations (String label)
specifier|public
name|Set
argument_list|<
name|BranchNameKey
argument_list|>
name|getDestinations
parameter_list|(
name|String
name|label
parameter_list|)
block|{
return|return
name|destinations
operator|.
name|get
argument_list|(
name|label
argument_list|)
return|;
block|}
DECL|method|parseLabel (String label, String text, ValidationError.Sink errors)
name|void
name|parseLabel
parameter_list|(
name|String
name|label
parameter_list|,
name|String
name|text
parameter_list|,
name|ValidationError
operator|.
name|Sink
name|errors
parameter_list|)
throws|throws
name|IOException
block|{
name|destinations
operator|.
name|replaceValues
argument_list|(
name|label
argument_list|,
name|toSet
argument_list|(
name|parse
argument_list|(
name|text
argument_list|,
name|DIR_NAME
operator|+
name|label
argument_list|,
name|TRIM
argument_list|,
literal|null
argument_list|,
name|errors
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|asText (String label)
name|String
name|asText
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|Set
argument_list|<
name|BranchNameKey
argument_list|>
name|dests
init|=
name|destinations
operator|.
name|get
argument_list|(
name|label
argument_list|)
decl_stmt|;
if|if
condition|(
name|dests
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|dests
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|BranchNameKey
name|dest
range|:
name|sort
argument_list|(
name|dests
argument_list|)
control|)
block|{
name|rows
operator|.
name|add
argument_list|(
operator|new
name|Row
argument_list|(
name|dest
operator|.
name|branch
argument_list|()
argument_list|,
name|dest
operator|.
name|project
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|asText
argument_list|(
literal|"Ref"
argument_list|,
literal|"Project"
argument_list|,
name|rows
argument_list|)
return|;
block|}
DECL|method|toSet (List<Row> destRows)
specifier|private
specifier|static
name|Set
argument_list|<
name|BranchNameKey
argument_list|>
name|toSet
parameter_list|(
name|List
argument_list|<
name|Row
argument_list|>
name|destRows
parameter_list|)
block|{
name|Set
argument_list|<
name|BranchNameKey
argument_list|>
name|dests
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|destRows
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|destRows
control|)
block|{
name|dests
operator|.
name|add
argument_list|(
name|BranchNameKey
operator|.
name|create
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
name|row
operator|.
name|right
argument_list|)
argument_list|,
name|row
operator|.
name|left
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|dests
return|;
block|}
block|}
end_class

end_unit

