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
name|server
operator|.
name|change
operator|.
name|HashtagsUtil
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
DECL|class|HashtagPredicate
specifier|public
class|class
name|HashtagPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|method|HashtagPredicate (String hashtag)
specifier|public
name|HashtagPredicate
parameter_list|(
name|String
name|hashtag
parameter_list|)
block|{
comment|// Use toLowerCase without locale to match behavior in ChangeField.
comment|// TODO(dborowitz): Change both.
name|super
argument_list|(
name|ChangeField
operator|.
name|HASHTAG
argument_list|,
name|HashtagsUtil
operator|.
name|cleanupHashtag
argument_list|(
name|hashtag
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData object)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|object
parameter_list|)
block|{
for|for
control|(
name|String
name|hashtag
range|:
name|object
operator|.
name|notes
argument_list|()
operator|.
name|load
argument_list|()
operator|.
name|getHashtags
argument_list|()
control|)
block|{
if|if
condition|(
name|hashtag
operator|.
name|equalsIgnoreCase
argument_list|(
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
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
literal|1
return|;
block|}
block|}
end_class

end_unit

