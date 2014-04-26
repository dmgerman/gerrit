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
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
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
name|Sets
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IArgumentMatcher
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

begin_comment
comment|/**  * Match for Iterables via set equals  *  * Converts both expected and actual parameter to a set and compares those two  * sets via equals to determine whether or not they match.  */
end_comment

begin_class
DECL|class|SetMatcher
specifier|public
class|class
name|SetMatcher
parameter_list|<
name|T
parameter_list|>
implements|implements
name|IArgumentMatcher
block|{
DECL|method|setEq (S expected)
specifier|public
specifier|static
parameter_list|<
name|S
extends|extends
name|Iterable
argument_list|<
name|T
argument_list|>
parameter_list|,
name|T
parameter_list|>
name|S
name|setEq
parameter_list|(
name|S
name|expected
parameter_list|)
block|{
name|EasyMock
operator|.
name|reportMatcher
argument_list|(
operator|new
name|SetMatcher
argument_list|<>
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|field|expected
name|Set
argument_list|<
name|T
argument_list|>
name|expected
decl_stmt|;
DECL|method|SetMatcher (Iterable<T> expected)
specifier|public
name|SetMatcher
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|expected
parameter_list|)
block|{
name|this
operator|.
name|expected
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|matches (Object actual)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Object
name|actual
parameter_list|)
block|{
if|if
condition|(
name|actual
operator|instanceof
name|Iterable
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|Set
argument_list|<
name|?
argument_list|>
name|actualSet
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|Iterable
argument_list|<
name|?
argument_list|>
operator|)
name|actual
argument_list|)
decl_stmt|;
return|return
name|expected
operator|.
name|equals
argument_list|(
name|actualSet
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|appendTo (StringBuffer buffer)
specifier|public
name|void
name|appendTo
parameter_list|(
name|StringBuffer
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

