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
DECL|package|com.google.gerrit.server.project.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|testing
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
name|common
operator|.
name|data
operator|.
name|LabelFunction
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
name|common
operator|.
name|data
operator|.
name|LabelType
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
name|common
operator|.
name|data
operator|.
name|LabelValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
DECL|class|Util
specifier|public
class|class
name|Util
block|{
DECL|method|codeReview ()
specifier|public
specifier|static
name|LabelType
name|codeReview
parameter_list|()
block|{
return|return
name|category
argument_list|(
literal|"Code-Review"
argument_list|,
name|value
argument_list|(
literal|2
argument_list|,
literal|"Looks good to me, approved"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Looks good to me, but someone else must approve"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"I would prefer this is not merged as is"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|2
argument_list|,
literal|"This shall not be merged"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|verified ()
specifier|public
specifier|static
name|LabelType
name|verified
parameter_list|()
block|{
return|return
name|category
argument_list|(
literal|"Verified"
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Verified"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"No score"
argument_list|)
argument_list|,
name|value
argument_list|(
operator|-
literal|1
argument_list|,
literal|"Fails"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|patchSetLock ()
specifier|public
specifier|static
name|LabelType
name|patchSetLock
parameter_list|()
block|{
name|LabelType
name|label
init|=
name|category
argument_list|(
literal|"Patch-Set-Lock"
argument_list|,
name|value
argument_list|(
literal|1
argument_list|,
literal|"Patch Set Locked"
argument_list|)
argument_list|,
name|value
argument_list|(
literal|0
argument_list|,
literal|"Patch Set Unlocked"
argument_list|)
argument_list|)
decl_stmt|;
name|label
operator|.
name|setFunction
argument_list|(
name|LabelFunction
operator|.
name|PATCH_SET_LOCK
argument_list|)
expr_stmt|;
return|return
name|label
return|;
block|}
DECL|method|value (int value, String text)
specifier|public
specifier|static
name|LabelValue
name|value
parameter_list|(
name|int
name|value
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|LabelValue
argument_list|(
operator|(
name|short
operator|)
name|value
argument_list|,
name|text
argument_list|)
return|;
block|}
DECL|method|category (String name, LabelValue... values)
specifier|public
specifier|static
name|LabelType
name|category
parameter_list|(
name|String
name|name
parameter_list|,
name|LabelValue
modifier|...
name|values
parameter_list|)
block|{
return|return
operator|new
name|LabelType
argument_list|(
name|name
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
return|;
block|}
DECL|method|Util ()
specifier|private
name|Util
parameter_list|()
block|{}
block|}
end_class

end_unit

