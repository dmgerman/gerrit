begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|exceptions
operator|.
name|StorageException
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
name|QueryParseException
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
name|index
operator|.
name|change
operator|.
name|ChangeField
import|;
end_import

begin_class
DECL|class|IsUnresolvedPredicate
specifier|public
class|class
name|IsUnresolvedPredicate
extends|extends
name|IntegerRangeChangePredicate
block|{
DECL|method|IsUnresolvedPredicate ()
specifier|public
name|IsUnresolvedPredicate
parameter_list|()
throws|throws
name|QueryParseException
block|{
name|this
argument_list|(
literal|">0"
argument_list|)
expr_stmt|;
block|}
DECL|method|IsUnresolvedPredicate (String value)
specifier|public
name|IsUnresolvedPredicate
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|UNRESOLVED_COMMENT_COUNT
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getValueInt (ChangeData changeData)
specifier|protected
name|Integer
name|getValueInt
parameter_list|(
name|ChangeData
name|changeData
parameter_list|)
throws|throws
name|StorageException
block|{
return|return
name|ChangeField
operator|.
name|UNRESOLVED_COMMENT_COUNT
operator|.
name|get
argument_list|(
name|changeData
argument_list|)
return|;
block|}
block|}
end_class

end_unit

