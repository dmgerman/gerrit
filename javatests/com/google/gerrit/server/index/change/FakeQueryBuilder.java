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
DECL|package|com.google.gerrit.server.index.change
package|package
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
name|change
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
name|query
operator|.
name|OperatorPredicate
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
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|query
operator|.
name|change
operator|.
name|ChangeQueryBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_class
annotation|@
name|Ignore
DECL|class|FakeQueryBuilder
specifier|public
class|class
name|FakeQueryBuilder
extends|extends
name|ChangeQueryBuilder
block|{
DECL|method|FakeQueryBuilder (ChangeIndexCollection indexes)
name|FakeQueryBuilder
parameter_list|(
name|ChangeIndexCollection
name|indexes
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|ChangeQueryBuilder
operator|.
name|Definition
argument_list|<>
argument_list|(
name|FakeQueryBuilder
operator|.
name|class
argument_list|)
argument_list|,
operator|new
name|ChangeQueryBuilder
operator|.
name|Arguments
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|indexes
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Operator
DECL|method|foo (String value)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|foo
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|predicate
argument_list|(
literal|"foo"
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|bar (String value)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|bar
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|predicate
argument_list|(
literal|"bar"
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|predicate (String name, String value)
specifier|private
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|predicate
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|OperatorPredicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
block|{}
return|;
block|}
block|}
end_class

end_unit

