begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.index.query.testing
package|package
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
name|testing
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
name|assertAbout
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|truth
operator|.
name|FailureMetadata
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
name|truth
operator|.
name|Subject
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
name|QueryParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|antlr
operator|.
name|runtime
operator|.
name|tree
operator|.
name|Tree
import|;
end_import

begin_class
DECL|class|TreeSubject
specifier|public
class|class
name|TreeSubject
extends|extends
name|Subject
block|{
DECL|method|assertThat (Tree actual)
specifier|public
specifier|static
name|TreeSubject
name|assertThat
parameter_list|(
name|Tree
name|actual
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|TreeSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|)
return|;
block|}
DECL|field|tree
specifier|private
specifier|final
name|Tree
name|tree
decl_stmt|;
DECL|method|TreeSubject (FailureMetadata failureMetadata, Tree tree)
specifier|private
name|TreeSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|Tree
name|tree
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|tree
argument_list|)
expr_stmt|;
name|this
operator|.
name|tree
operator|=
name|tree
expr_stmt|;
block|}
DECL|method|hasType (int expectedType)
specifier|public
name|void
name|hasType
parameter_list|(
name|int
name|expectedType
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"getType()"
argument_list|)
operator|.
name|that
argument_list|(
name|typeName
argument_list|(
name|tree
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|typeName
argument_list|(
name|expectedType
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|hasText (String expectedText)
specifier|public
name|void
name|hasText
parameter_list|(
name|String
name|expectedText
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|expectedText
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"getText()"
argument_list|)
operator|.
name|that
argument_list|(
name|tree
operator|.
name|getText
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedText
argument_list|)
expr_stmt|;
block|}
DECL|method|hasNoChildren ()
specifier|public
name|void
name|hasNoChildren
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"getChildCount()"
argument_list|)
operator|.
name|that
argument_list|(
name|tree
operator|.
name|getChildCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|hasChildCount (int expectedChildCount)
specifier|public
name|void
name|hasChildCount
parameter_list|(
name|int
name|expectedChildCount
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|expectedChildCount
operator|>
literal|0
argument_list|,
literal|"expected child count must be positive: %s"
argument_list|,
name|expectedChildCount
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"getChildCount()"
argument_list|)
operator|.
name|that
argument_list|(
name|tree
operator|.
name|getChildCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedChildCount
argument_list|)
expr_stmt|;
block|}
DECL|method|child (int childIndex)
specifier|public
name|TreeSubject
name|child
parameter_list|(
name|int
name|childIndex
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getChild(%s)"
argument_list|,
name|childIndex
argument_list|)
operator|.
name|about
argument_list|(
name|TreeSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|tree
operator|.
name|getChild
argument_list|(
name|childIndex
argument_list|)
argument_list|)
return|;
block|}
DECL|method|typeName (int type)
specifier|private
specifier|static
name|String
name|typeName
parameter_list|(
name|int
name|type
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|type
operator|>=
literal|0
operator|&&
name|type
operator|<
name|QueryParser
operator|.
name|tokenNames
operator|.
name|length
argument_list|,
literal|"invalid token type %s, max is %s"
argument_list|,
name|type
argument_list|,
name|QueryParser
operator|.
name|tokenNames
operator|.
name|length
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|QueryParser
operator|.
name|tokenNames
index|[
name|type
index|]
return|;
block|}
block|}
end_class

end_unit

