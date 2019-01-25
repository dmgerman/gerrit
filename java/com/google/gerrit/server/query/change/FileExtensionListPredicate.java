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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
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
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_class
DECL|class|FileExtensionListPredicate
specifier|public
class|class
name|FileExtensionListPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|method|clean (String extList)
specifier|private
specifier|static
name|String
name|clean
parameter_list|(
name|String
name|extList
parameter_list|)
block|{
return|return
name|Splitter
operator|.
name|on
argument_list|(
literal|','
argument_list|)
operator|.
name|splitToList
argument_list|(
name|extList
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|FileExtensionPredicate
operator|::
name|clean
argument_list|)
operator|.
name|distinct
argument_list|()
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
return|;
block|}
DECL|method|FileExtensionListPredicate (String value)
name|FileExtensionListPredicate
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|ONLY_EXTENSIONS
argument_list|,
name|clean
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
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
name|OrmException
block|{
return|return
name|ChangeField
operator|.
name|getAllExtensionsAsList
argument_list|(
name|cd
argument_list|)
operator|.
name|equals
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

