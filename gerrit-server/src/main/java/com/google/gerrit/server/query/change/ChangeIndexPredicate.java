begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
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
name|Matchable
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
name|Change
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
operator|.
name|Arguments
import|;
end_import

begin_class
DECL|class|ChangeIndexPredicate
specifier|public
specifier|abstract
class|class
name|ChangeIndexPredicate
extends|extends
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
implements|implements
name|Matchable
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|method|ChangeIndexPredicate (FieldDef<ChangeData, ?> def, String value)
specifier|protected
name|ChangeIndexPredicate
parameter_list|(
name|FieldDef
argument_list|<
name|ChangeData
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
DECL|method|ChangeIndexPredicate (FieldDef<ChangeData, ?> def, String name, String value)
specifier|protected
name|ChangeIndexPredicate
parameter_list|(
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
name|def
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|def
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|create (Arguments args, Predicate<ChangeData> p)
specifier|protected
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|create
parameter_list|(
name|Arguments
name|args
parameter_list|,
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
block|{
if|if
condition|(
operator|!
name|args
operator|.
name|allowsDrafts
condition|)
block|{
return|return
name|Predicate
operator|.
name|and
argument_list|(
name|p
argument_list|,
name|Predicate
operator|.
name|not
argument_list|(
operator|new
name|ChangeStatusPredicate
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|DRAFT
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
return|return
name|p
return|;
block|}
block|}
end_class

end_unit

