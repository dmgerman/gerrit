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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|CharMatcher
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
name|base
operator|.
name|Splitter
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
name|Iterables
import|;
end_import

begin_comment
comment|/** Utilities for option parsing. */
end_comment

begin_class
DECL|class|OptionUtil
specifier|public
class|class
name|OptionUtil
block|{
DECL|field|COMMA_OR_SPACE
specifier|private
specifier|static
specifier|final
name|Splitter
name|COMMA_OR_SPACE
init|=
name|Splitter
operator|.
name|on
argument_list|(
name|CharMatcher
operator|.
name|anyOf
argument_list|(
literal|", "
argument_list|)
argument_list|)
operator|.
name|omitEmptyStrings
argument_list|()
operator|.
name|trimResults
argument_list|()
decl_stmt|;
DECL|field|TO_LOWER_CASE
specifier|private
specifier|static
specifier|final
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|TO_LOWER_CASE
init|=
operator|new
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|method|splitOptionValue (String value)
specifier|public
specifier|static
name|Iterable
argument_list|<
name|String
argument_list|>
name|splitOptionValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|transform
argument_list|(
name|COMMA_OR_SPACE
operator|.
name|split
argument_list|(
name|value
argument_list|)
argument_list|,
name|TO_LOWER_CASE
argument_list|)
return|;
block|}
DECL|method|OptionUtil ()
specifier|private
name|OptionUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

