begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_class
DECL|class|AfterPredicate
specifier|public
class|class
name|AfterPredicate
extends|extends
name|TimestampRangeChangePredicate
block|{
DECL|field|cut
specifier|protected
specifier|final
name|Date
name|cut
decl_stmt|;
DECL|method|AfterPredicate (String value)
specifier|public
name|AfterPredicate
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
name|UPDATED
argument_list|,
name|ChangeQueryBuilder
operator|.
name|FIELD_BEFORE
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|cut
operator|=
name|parse
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMinTimestamp ()
specifier|public
name|Date
name|getMinTimestamp
parameter_list|()
block|{
return|return
name|cut
return|;
block|}
annotation|@
name|Override
DECL|method|getMaxTimestamp ()
specifier|public
name|Date
name|getMaxTimestamp
parameter_list|()
block|{
return|return
operator|new
name|Date
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|StorageException
block|{
return|return
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getLastUpdatedOn
argument_list|()
operator|.
name|getTime
argument_list|()
operator|>=
name|cut
operator|.
name|getTime
argument_list|()
return|;
block|}
block|}
end_class

end_unit

