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
DECL|package|com.google.gerrit.acceptance.rest.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|project
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|api
operator|.
name|projects
operator|.
name|RefInfo
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
DECL|class|RefAssert
specifier|public
class|class
name|RefAssert
block|{
DECL|method|assertRefs ( List<? extends RefInfo> expectedRefs, List<? extends RefInfo> actualRefs)
specifier|public
specifier|static
name|void
name|assertRefs
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|RefInfo
argument_list|>
name|expectedRefs
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|RefInfo
argument_list|>
name|actualRefs
parameter_list|)
block|{
name|assertRefNames
argument_list|(
name|refs
argument_list|(
name|expectedRefs
argument_list|)
argument_list|,
name|actualRefs
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedRefs
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|assertRefInfo
argument_list|(
name|expectedRefs
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|actualRefs
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertRefNames ( Iterable<String> expectedRefs, Iterable<? extends RefInfo> actualRefs)
specifier|public
specifier|static
name|void
name|assertRefNames
parameter_list|(
name|Iterable
argument_list|<
name|String
argument_list|>
name|expectedRefs
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|RefInfo
argument_list|>
name|actualRefs
parameter_list|)
block|{
name|Iterable
argument_list|<
name|String
argument_list|>
name|actualNames
init|=
name|refs
argument_list|(
name|actualRefs
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|actualNames
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expectedRefs
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|assertRefInfo (RefInfo expected, RefInfo actual)
specifier|public
specifier|static
name|void
name|assertRefInfo
parameter_list|(
name|RefInfo
name|expected
parameter_list|,
name|RefInfo
name|actual
parameter_list|)
block|{
name|assertThat
argument_list|(
name|actual
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|ref
argument_list|)
expr_stmt|;
if|if
condition|(
name|expected
operator|.
name|revision
operator|!=
literal|null
condition|)
block|{
name|assertThat
argument_list|(
name|actual
operator|.
name|revision
argument_list|)
operator|.
name|named
argument_list|(
literal|"revision of "
operator|+
name|actual
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
operator|.
name|revision
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|toBoolean
argument_list|(
name|actual
operator|.
name|canDelete
argument_list|)
argument_list|)
operator|.
name|named
argument_list|(
literal|"can delete "
operator|+
name|actual
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|toBoolean
argument_list|(
name|expected
operator|.
name|canDelete
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|refs (Iterable<? extends RefInfo> infos)
specifier|private
specifier|static
name|Iterable
argument_list|<
name|String
argument_list|>
name|refs
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|RefInfo
argument_list|>
name|infos
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|transform
argument_list|(
name|infos
argument_list|,
name|b
lambda|->
name|b
operator|.
name|ref
argument_list|)
return|;
block|}
DECL|method|toBoolean (Boolean b)
specifier|private
specifier|static
name|boolean
name|toBoolean
parameter_list|(
name|Boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|b
operator|.
name|booleanValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

