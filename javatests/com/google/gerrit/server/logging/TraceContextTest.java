begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.logging
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
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
name|ImmutableMap
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
name|gerrit
operator|.
name|server
operator|.
name|util
operator|.
name|RequestId
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|TraceContextTest
specifier|public
class|class
name|TraceContextTest
block|{
annotation|@
name|After
DECL|method|cleanup ()
specifier|public
name|void
name|cleanup
parameter_list|()
block|{
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|clearTags
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|openContext ()
specifier|public
name|void
name|openContext
parameter_list|()
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
init|)
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|openNestedContexts ()
specifier|public
name|void
name|openNestedContexts
parameter_list|()
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
init|)
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext2
init|=
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
init|)
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"abc"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"xyz"
argument_list|)
argument_list|,
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|openNestedContextsWithSameTagName ()
specifier|public
name|void
name|openNestedContextsWithSameTagName
parameter_list|()
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
init|)
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext2
init|=
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
literal|"foo"
argument_list|,
literal|"baz"
argument_list|)
init|)
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|,
literal|"baz"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|openNestedContextsWithSameTagNameAndValue ()
specifier|public
name|void
name|openNestedContextsWithSameTagNameAndValue
parameter_list|()
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
init|)
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext2
init|=
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
init|)
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|openContextWithRequestId ()
specifier|public
name|void
name|openContextWithRequestId
parameter_list|()
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
name|RequestId
operator|.
name|Type
operator|.
name|RECEIVE_ID
argument_list|,
literal|"foo"
argument_list|)
init|)
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"RECEIVE_ID"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addTag ()
specifier|public
name|void
name|addTag
parameter_list|()
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
init|)
block|{
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|traceContext
operator|.
name|addTag
argument_list|(
literal|"foo"
argument_list|,
literal|"baz"
argument_list|)
expr_stmt|;
name|traceContext
operator|.
name|addTag
argument_list|(
literal|"bar"
argument_list|,
literal|"baz"
argument_list|)
expr_stmt|;
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|,
literal|"baz"
argument_list|)
argument_list|,
literal|"bar"
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTags
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertTags (ImmutableMap<String, ImmutableSet<String>> expectedTagMap)
specifier|private
name|void
name|assertTags
parameter_list|(
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|ImmutableSet
argument_list|<
name|String
argument_list|>
argument_list|>
name|expectedTagMap
parameter_list|)
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|SortedSet
argument_list|<
name|Object
argument_list|>
argument_list|>
name|actualTagMap
init|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|getTags
argument_list|()
operator|.
name|asMap
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|actualTagMap
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expectedTagMap
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ImmutableSet
argument_list|<
name|String
argument_list|>
argument_list|>
name|expectedEntry
range|:
name|expectedTagMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|assertThat
argument_list|(
name|actualTagMap
operator|.
name|get
argument_list|(
name|expectedEntry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|expectedEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

