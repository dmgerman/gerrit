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
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
DECL|class|ProjectWatchInfo
specifier|public
class|class
name|ProjectWatchInfo
block|{
DECL|field|project
specifier|public
name|String
name|project
decl_stmt|;
DECL|field|filter
specifier|public
name|String
name|filter
decl_stmt|;
DECL|field|notifyNewChanges
specifier|public
name|Boolean
name|notifyNewChanges
decl_stmt|;
DECL|field|notifyNewPatchSets
specifier|public
name|Boolean
name|notifyNewPatchSets
decl_stmt|;
DECL|field|notifyAllComments
specifier|public
name|Boolean
name|notifyAllComments
decl_stmt|;
DECL|field|notifySubmittedChanges
specifier|public
name|Boolean
name|notifySubmittedChanges
decl_stmt|;
DECL|field|notifyAbandonedChanges
specifier|public
name|Boolean
name|notifyAbandonedChanges
decl_stmt|;
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|ProjectWatchInfo
condition|)
block|{
name|ProjectWatchInfo
name|w
init|=
operator|(
name|ProjectWatchInfo
operator|)
name|obj
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|project
argument_list|,
name|w
operator|.
name|project
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|filter
argument_list|,
name|w
operator|.
name|filter
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|notifyNewChanges
argument_list|,
name|w
operator|.
name|notifyNewChanges
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|notifyNewPatchSets
argument_list|,
name|w
operator|.
name|notifyNewPatchSets
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|notifyAllComments
argument_list|,
name|w
operator|.
name|notifyAllComments
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|notifySubmittedChanges
argument_list|,
name|w
operator|.
name|notifySubmittedChanges
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|notifyAbandonedChanges
argument_list|,
name|w
operator|.
name|notifyAbandonedChanges
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|project
argument_list|,
name|filter
argument_list|,
name|notifyNewChanges
argument_list|,
name|notifyNewPatchSets
argument_list|,
name|notifyAllComments
argument_list|,
name|notifySubmittedChanges
argument_list|,
name|notifyAbandonedChanges
argument_list|)
return|;
block|}
block|}
end_class

end_unit

