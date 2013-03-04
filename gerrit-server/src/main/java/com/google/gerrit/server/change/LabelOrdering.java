begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|base
operator|.
name|Function
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
name|Ordering
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
name|common
operator|.
name|data
operator|.
name|LabelType
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
name|common
operator|.
name|data
operator|.
name|LabelTypes
import|;
end_import

begin_class
DECL|class|LabelOrdering
class|class
name|LabelOrdering
block|{
DECL|method|create (final LabelTypes labelTypes)
specifier|public
specifier|static
name|Ordering
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
specifier|final
name|LabelTypes
name|labelTypes
parameter_list|)
block|{
return|return
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsLast
argument_list|()
operator|.
name|onResultOf
argument_list|(
operator|new
name|Function
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Short
name|apply
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|LabelType
name|lt
init|=
name|labelTypes
operator|.
name|byLabel
argument_list|(
name|n
argument_list|)
decl_stmt|;
return|return
name|lt
operator|!=
literal|null
condition|?
name|lt
operator|.
name|getPosition
argument_list|()
else|:
literal|null
return|;
block|}
block|}
argument_list|)
operator|.
name|compound
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
return|;
block|}
DECL|method|LabelOrdering ()
specifier|private
name|LabelOrdering
parameter_list|()
block|{   }
block|}
end_class

end_unit

